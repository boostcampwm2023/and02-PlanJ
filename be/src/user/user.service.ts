import {ConflictException, Injectable, InternalServerErrorException, UnauthorizedException} from "@nestjs/common";
import { UserLoginDto } from "./dto/user-login.dto";
import { CreateUserDto } from "./dto/create-user.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { UserModifyDto } from "./dto/user-modify.dto";
import { UserEntity } from "./entity/user.entity";
import {ulid} from "ulid";
import * as bcrypt from "bcryptjs";
import {HttpResponse} from "../utils/http.response";

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(UserRepository) private userRepository: UserRepository,
  ) {}

  async register(dto: CreateUserDto): Promise<void> {
    const { email, password, nickname } = dto;

    const userExist = await this.userRepository.checkUserExists(email);
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

    throw new UnauthorizedException("로그인 실패");
  }

  async validateUser(userUuid: string) {
    const user = await this.userRepository.findOne({ where: {userUuid: userUuid}})
    if (user) {
      return true;
    }
    
    throw new UnauthorizedException("유효하지 않은 사용자")
  }

  async deleteAccount(dto: UserLoginDto): Promise<string> {
    return await this.userRepository.deleteUser(dto);
  }

  async update(userUuid:string, dto: UserModifyDto): Promise<HttpResponse> {
    const { nickname } = dto;
    const user = await this.userRepository.findOne({ where: { userUuid: userUuid } });

    user.nickname = nickname;
    try {
      await this.userRepository.save(user);
      const body: HttpResponse = {
        message: "정보 수정 성공",
        statusCode: 200,
        data: {
          updatedNickname: nickname,
        },
      };

      return body;
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  async getUserEntity(userUuid: string): Promise<UserEntity> {
    return await this.userRepository.checkByUserUuid(userUuid);
  }
}
