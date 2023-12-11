import {
  ConflictException,
  Injectable,
  InternalServerErrorException,
  Logger,
  UnauthorizedException,
} from "@nestjs/common";
import { UserLoginDto } from "./dto/user-login.dto";
import { CreateUserDto } from "./dto/create-user.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { UserEntity } from "./entity/user.entity";
import { ulid } from "ulid";
import * as bcrypt from "bcryptjs";
import { NaverResponseDto } from "./dto/naver-response.dto";

// TODO: soft delete 된 사용자가 다시 가입을 하는 경우 처리 -> 현재는 새로 생성
@Injectable()
export class UserService {
  private readonly logger = new Logger(UserService.name);
  constructor(@InjectRepository(UserRepository) private userRepository: UserRepository) {}

  async register(dto: CreateUserDto): Promise<void> {
    const { email, password, nickname } = dto;

    const userExist = await this.userRepository.checkExistsByEmail(email);
    if (userExist) {
      throw new ConflictException("해당 이메일로는 가입할 수 없습니다.");
    }

    const userUuid = ulid();
    const salt = await bcrypt.genSalt();
    const hashedPassword = await bcrypt.hash(password, salt);

    const user = this.userRepository.create({
      userUuid: userUuid,
      email: email,
      password: hashedPassword,
      nickname: nickname,
    });

    try {
      await this.userRepository.save(user);
      return;
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }

  async login(dto: UserLoginDto): Promise<string> {
    const { email, password } = dto;
    const user = await this.userRepository.findOne({ where: { email: email } });

    if (user && (await bcrypt.compare(password, user.password))) {
      return user.userUuid;
    }

    throw new UnauthorizedException("로그인에 실패하였습니다.");
  }

  async validateUser(userUuid: string): Promise<boolean> {
    const user = await this.userRepository.findOne({ where: { userUuid: userUuid } });
    if (user) {
      return true;
    }

    throw new UnauthorizedException("유효하지 않은 사용자입니다.");
  }

  async deleteAccount(userUuid: string): Promise<void> {
    await this.userRepository.deleteByUuid(userUuid);
  }

  async update(userUuid: string, nickname: string, profileUrl: string | null): Promise<void> {
    const user = await this.userRepository.findOne({ where: { userUuid: userUuid } });
    user.nickname = nickname;
    user.profileUrl = profileUrl ?? user.profileUrl;

    this.logger.verbose(JSON.stringify(user, null, 2));
    try {
      await this.userRepository.save(user);
      return;
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  async getUserEntity(userUuid: string): Promise<UserEntity> {
    return await this.userRepository.findByUuid(userUuid);
  }

  async getUserEntityById(userId: number): Promise<UserEntity> {
    return await this.userRepository.findById(userId);
  }

  async getUserEntityByEmail(userEmail: string): Promise<UserEntity> {
    return await this.userRepository.findByEmail(userEmail);
  }

  async loginByNaver(response: NaverResponseDto, deviceToken: string) {
    const user = await this.userRepository.findOne({ where: { naverId: response.id } });

    if (!user) {
      const uuid = ulid();
      const newUser = this.userRepository.create({
        userUuid: uuid,
        email: response.email,
        password: null,
        nickname: response.nickname,
        profileUrl: response.profileImage,
        naverId: response.id,
        deviceToken: deviceToken ?? null,
      });

      await this.userRepository.save(newUser);
      return newUser;
    }

    return user;
  }

  async setUserProfileImageNull(userUuid: any) {
    const userEntity = await this.userRepository.findOne({ where: { userUuid } });

    if (!userEntity) {
      throw new UnauthorizedException("유효하지 않은 사용자입니다.");
    }

    // 사용자의 프로필 사진이 이미 기본 사진일 때
    if (!userEntity.profileUrl) {
      return;
    }

    userEntity.profileUrl = null;
    try {
      await this.userRepository.save(userEntity);
      return;
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  async saveDeviceToken(userUuid: string, deviceToken: string) {
    if (!deviceToken) {
      return;
    }

    const record = await this.userRepository.findByUuid(userUuid);
    record.deviceToken = deviceToken;
    await this.userRepository.save(record);
  }

  async logout(userUuid: string) {
    const user = await this.userRepository.findOne({ where: { userUuid } });
    if (!user.deviceToken) {
      return;
    }

    user.deviceToken = null;
    await this.userRepository.save(user);
  }
}
