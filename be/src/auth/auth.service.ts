import { Injectable } from "@nestjs/common";
import { UserLoginDto } from "./dto/user-login.dto";
import { CreateUserDto } from "./dto/create-user.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";

@Injectable()
export class AuthService {
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
}
