import { Controller, Post, Body, ValidationPipe } from "@nestjs/common";
import { AuthService } from "./auth.service";
import { UserLoginDto } from "./dto/user-login.dto";
import { CreateUserDto } from "./dto/create-user.dto";

@Controller("/api/auth")
export class AuthController {
  constructor(private authService: AuthService) {}

  @Post("/register")
  async register(@Body(ValidationPipe) dto: CreateUserDto): Promise<string> {
    return this.authService.register(dto);
  }

  @Post("/login")
  login(@Body(ValidationPipe) dto: UserLoginDto) {
    return this.authService.login(dto);
  }
}
