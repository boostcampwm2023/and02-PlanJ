import { Controller, Post, Body, HttpCode, HttpStatus, Delete, Patch, UseGuards, Headers } from "@nestjs/common";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { UserModifyDto } from "../user/dto/user-modify.dto";
import { UserApiService } from "./user-api.service";
import { AuthGuard } from "../guard/auth.guard";

@Controller("/api/auth")
export class UserApiController {
  constructor(private userApiService: UserApiService) {}

  @Post("/register")
  async register(@Body() dto: CreateUserDto): Promise<JSON> {
    const result = await this.userApiService.register(dto);
    return JSON.parse(result);
  }

  @Post("/login")
  @HttpCode(HttpStatus.OK) // api 명세서에 따라 응답 상태 코드 변경
  async login(@Body() dto: UserLoginDto): Promise<JSON> {
    const result = await this.userApiService.login(dto);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Delete("/delete")
  async deleteUser(@Headers() headers: any): Promise<JSON> {
    const jwtToken = headers.authorization as string;
    const result = await this.userApiService.delete(jwtToken);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Patch("/update")
  @HttpCode(HttpStatus.OK)
  async updateUserInfo(@Headers() headers: any, @Body() dto: UserModifyDto): Promise<JSON> {
    const jwtToken = headers.authorization as string;
    const result = await this.userApiService.update(dto, jwtToken);
    return JSON.parse(result);
  }
}
