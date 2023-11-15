import { Controller, Post, Body, HttpCode, HttpStatus, Delete, Patch } from "@nestjs/common";
import { AuthService } from "./auth.service";
import { UserLoginDto } from "./dto/user-login.dto";
import { CreateUserDto } from "./dto/create-user.dto";
import { UserModifyDto } from "./dto/user-modify.dto";

@Controller("/api/auth")
export class AuthController {
  constructor(private authService: AuthService) {}

  @Post("/register")
  async register(@Body() dto: CreateUserDto): Promise<JSON> {
    const result = await this.authService.register(dto);
    return JSON.parse(result);
  }

  @Post("/login")
  @HttpCode(HttpStatus.OK)
  async login(@Body() dto: UserLoginDto): Promise<JSON> {
    const result = await this.authService.login(dto);
    return JSON.parse(result);
  }

  @Delete("/delete")
  async deleteAccount(@Body() dto: UserLoginDto) {
    const result = await this.authService.deleteAccount(dto);
    return JSON.parse(result);
  }

  @Patch("/update")
  @HttpCode(HttpStatus.OK)
  async updateUserInfo(@Body() dto: UserModifyDto): Promise<JSON> {
    const result = await this.authService.update(dto);
    return JSON.parse(result);
  }
}
