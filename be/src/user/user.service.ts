import { ConflictException, Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import { UserLoginDto } from "./dto/user-login.dto";
import { CreateUserDto } from "./dto/create-user.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { UserModifyDto } from "./dto/user-modify.dto";
import { UserEntity } from "./entity/user.entity";
import { ulid } from "ulid";
import * as bcrypt from "bcryptjs";

// TODO: soft delete 된 사용자가 다시 가입을 하는 경우 처리 -> 현재는 새로 생성
@Injectable()
export class UserService {
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

  async update(userUuid: string, dto: UserModifyDto): Promise<string> {
    const { nickname } = dto;
    const user = await this.userRepository.findOne({ where: { userUuid: userUuid } });

    user.nickname = nickname;
    try {
      await this.userRepository.save(user);
      return nickname;
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  async checkUser(userUuid: string): Promise<UserEntity> {
    return await this.userRepository.findByUuid(userUuid);
  }
}
