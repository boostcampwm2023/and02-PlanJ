import {
  Controller,
  Post,
  Body,
  HttpCode,
  HttpStatus,
  Delete,
  Patch,
  UseGuards,
  Get,
  UseInterceptors,
  UploadedFile,
} from "@nestjs/common";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { UserApiService } from "./user-api.service";
import { AuthGuard } from "../guard/auth.guard";
import { Token } from "../utils/token.decorator";
import { FileInterceptor } from "@nestjs/platform-express";

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
  @Get()
  async getUserInfo(@Token() token: string): Promise<JSON> {
    const result = await this.userApiService.getUserInfo(token);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Patch()
  @UseInterceptors(FileInterceptor("profileImage"))
  @HttpCode(HttpStatus.OK)
  async updateUserInfo(
    @Token() token: string,
    @UploadedFile() profileImage: Express.Multer.File,
    @Body("nickname") nickname: string,
  ): Promise<JSON> {
    nickname = nickname.replace(/"/g, "");
    const result = await this.userApiService.updateUserInfo(token, profileImage, nickname);
    return JSON.parse(result);
  }
}
