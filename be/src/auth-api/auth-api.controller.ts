import { Controller, Post, Body, HttpCode, HttpStatus, Delete, Patch } from "@nestjs/common";
import { UserService } from "../user/user.service";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { UserModifyDto } from "../user/dto/user-modify.dto";

@Controller("/api/auth")
export class AuthApiController {
  constructor(private userService: UserService) {}

  @Post("/register")
  async register(@Body() dto: CreateUserDto): Promise<JSON> {
    const result = await this.userService.register(dto);
    return JSON.parse(result);
  }

  @Post("/login")
  @HttpCode(HttpStatus.OK)
  async login(@Body() dto: UserLoginDto): Promise<JSON> {
    const result = await this.userService.login(dto);
    return JSON.parse(result);
  }

  @Delete("/delete")
  async deleteAccount(@Body() dto: UserLoginDto) {
    const result = await this.userService.deleteAccount(dto);
    return JSON.parse(result);
  }

  @Patch("/update")
  @HttpCode(HttpStatus.OK)
  async updateUserInfo(@Body() dto: UserModifyDto): Promise<JSON> {
    const result = await this.userService.update(dto);
    return JSON.parse(result);
  }
}
