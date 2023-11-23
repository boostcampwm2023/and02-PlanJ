import {Controller, Post, Body, HttpCode, HttpStatus, Delete, Patch, UseGuards, Headers} from "@nestjs/common";
import { UserService } from "../user/user.service";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { UserModifyDto } from "../user/dto/user-modify.dto";
import { AuthApiService } from "./auth-api.service";
import {AuthGuard} from "../guard/auth.guard";

@Controller("/api/auth")
export class AuthApiController {
  constructor(
      private userService: UserService,
      private authApiService: AuthApiService
  ) {}

  @Post("/register")
  async register(@Body() dto: CreateUserDto): Promise<JSON> {
    const result = await this.authApiService.register(dto);
    return JSON.parse(result);
  }

  @Post("/login")
  @HttpCode(HttpStatus.OK)  // api 명세서에 따라 응답 상태 코드 변경
  async login(@Body() dto: UserLoginDto): Promise<JSON> {
    const result = await this.authApiService.login(dto);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Delete("/delete")
  async deleteAccount(@Headers() headers: any, @Body() dto: UserLoginDto) {
    const result = await this.userService.deleteAccount(dto);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Patch("/update")
  @HttpCode(HttpStatus.OK)
  async updateUserInfo(@Headers() headers: any, @Body() dto: UserModifyDto): Promise<JSON> {
    const jwtToken = headers.authorization as string;
    const result = await this.authApiService.update(dto, jwtToken);
    return JSON.parse(result);
  }
}
