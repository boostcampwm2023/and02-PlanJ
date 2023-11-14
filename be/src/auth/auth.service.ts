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
    return this.userRepository.register(dto);
  }

  login(dto: UserLoginDto) {
    return this.userRepository.login(dto);
  }
}
