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
  Logger,
} from "@nestjs/common";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { UserApiService } from "./user-api.service";
import { AuthGuard } from "../guard/auth.guard";
import { Token } from "../utils/token.decorator";
import { FileInterceptor } from "@nestjs/platform-express";

@Controller("/api/auth")
export class UserApiController {
  private readonly logger = new Logger(UserApiController.name);

  constructor(private userApiService: UserApiService) {}

  @Post("/register")
  async register(@Body() dto: CreateUserDto): Promise<JSON> {
    this.logger.log("Post /register");
    this.logger.verbose("Create user dto: ", JSON.stringify(dto, null, 2));
    const result = await this.userApiService.register(dto);
    return JSON.parse(result);
  }

  @Post("/naver")
  async loginByNaver(@Body("accessToken") accessToken: string): Promise<JSON> {
    this.logger.log("Post /naver");
    this.logger.verbose("Access Token: " + accessToken);
    const result = await this.userApiService.loginByNaver(accessToken);
    return JSON.parse(result);
  }

  @Post("/login")
  @HttpCode(HttpStatus.OK)
  async login(@Body() dto: UserLoginDto): Promise<JSON> {
    this.logger.log("Post /login");
    this.logger.verbose("User login dto: " + JSON.stringify(dto, null, 2));
    const result = await this.userApiService.login(dto);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Delete("/delete")
  async deleteUser(@Token() token: string): Promise<JSON> {
    this.logger.log("Delete /delete");
    this.logger.verbose("Token: " + token);
    const result = await this.userApiService.delete(token);
    return JSON.parse(result);
  }

  @UseGuards(AuthGuard)
  @Get()
  async getUserInfo(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /");
    this.logger.verbose("Token: " + token);
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
