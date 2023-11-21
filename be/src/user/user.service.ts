import { Injectable } from "@nestjs/common";
import { UserLoginDto } from "./dto/user-login.dto";
import { CreateUserDto } from "./dto/create-user.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { UserModifyDto } from "./dto/user-modify.dto";
import { UserEntity } from "./entity/user.entity";

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(UserRepository)
    private userRepository: UserRepository,
  ) {}

  async register(dto: CreateUserDto): Promise<string> {
    return await this.userRepository.register(dto);
  }

  async login(dto: UserLoginDto): Promise<string> {
    return JSON.stringify(await this.userRepository.login(dto));
  }

  async deleteAccount(dto: UserLoginDto): Promise<string> {
    return await this.userRepository.deleteAccount(dto);
  }

  update(dto: UserModifyDto): Promise<string> {
    return this.userRepository.updateInfo(dto);
  }

  async getUserEntity(userUuid: string): Promise<UserEntity> {
    return await this.userRepository.checkByUserUuid(userUuid);
  }
}
