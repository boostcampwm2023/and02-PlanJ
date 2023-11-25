import { Controller, Post, Body, HttpCode, HttpStatus, Delete, Patch, UseGuards, Headers } from "@nestjs/common";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { UserModifyDto } from "../user/dto/user-modify.dto";
import { UserApiService } from "./user-api.service";
import { AuthGuard } from "../guard/auth.guard";
import { Token } from "../utils/token.decorator";

@Controller("/api/auth")
export class UserApiController {
  constructor(private userApiService: UserApiService) {}

  @Post("/register")
  async register(@Body() dto: CreateUserDto): Promise<JSON> {
    const result = await this.userApiService.register(dto);
    return JSON.parse(result);
  }

  @Post("/login")
  @HttpCode(HttpStatus.OK)
  async login(@Body() dto: UserLoginDto): Promise<JSON> {
    const result = await this.userApiService.login(dto);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Delete("/delete")
  async deleteUser(@Token() token: string): Promise<JSON> {
    const result = await this.userApiService.delete(token);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Patch("/update")
  @HttpCode(HttpStatus.OK)
  async updateUserInfo(@Token() token: string, @Body() dto: UserModifyDto): Promise<JSON> {
    const result = await this.userApiService.update(dto, token);
    return JSON.parse(result);
  }
}
