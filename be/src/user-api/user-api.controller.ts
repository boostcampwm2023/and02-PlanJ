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
import { HttpResponse } from "../utils/http.response";
import {
  ApiBadRequestResponse,
  ApiBearerAuth,
  ApiConflictResponse,
  ApiConsumes,
  ApiCreatedResponse,
  ApiOkResponse,
  ApiOperation,
  ApiTags,
  ApiUnauthorizedResponse,
  ApiUnprocessableEntityResponse,
} from "@nestjs/swagger";

@ApiTags("사용자")
@Controller("/api/auth")
export class UserApiController {
  private readonly logger = new Logger(UserApiController.name);

  constructor(private userApiService: UserApiService) {}

  @ApiOperation({ summary: "회원 가입" })
  @ApiCreatedResponse({
    description: "회원 가입 완료",
    schema: {
      example: {
        message: "회원가입 성공",
      },
    },
  })
  @ApiBadRequestResponse({
    description: "이메일 형식 오류",
    schema: {
      example: {
        message: ["email must be an email"],
        error: "Bad Request",
        statusCode: 400,
      },
    },
  })
  @ApiBadRequestResponse({
    description: "비밀번호 길이 제한 위배 오류",
    schema: {
      example: {
        message: ["password must be longer than or equal to 8 characters"],
        error: "Bad Request",
        statusCode: 400,
      },
    },
  })
  @ApiConflictResponse({
    description: "이메일 중복 가입 오류",
    schema: {
      example: {
        message: "해당 이메일로는 가입할 수 없습니다.",
        error: "Conflict",
        statusCode: 409,
      },
    },
  })
  @Post("/register")
  async register(@Body() dto: CreateUserDto): Promise<JSON> {
    this.logger.log("Post /api/auth/register");
    this.logger.verbose("Create user dto: ", JSON.stringify(dto, null, 2));
    const result = await this.userApiService.register(dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "네이버 로그인" })
  @ApiCreatedResponse({
    description: "네이버 로그인 성공",
    schema: {
      example: {
        message: "네이버 연동 로그인 성공",
        data: {
          token: "01HFNXY2MA71N6QA5ZS0AG6QCK", // JWT token
        },
      },
    },
  })
  @Post("/naver")
  async loginByNaver(
    @Body("accessToken") accessToken: string,
    @Body("deviceToken") deviceToken: string,
  ): Promise<JSON> {
    this.logger.log("Post /api/auth/naver");
    this.logger.verbose("Access Token: " + accessToken);
    const result = await this.userApiService.loginByNaver(accessToken, deviceToken);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "로그인" })
  @ApiOkResponse({
    description: "로그인 성공",
    schema: {
      example: {
        message: "로그인 성공",
        data: {
          token: "01HFNXY2MA71N6QA5ZS0AG6QCK", // JWT token
        },
      },
    },
  })
  @ApiUnauthorizedResponse({
    description: "이메일 또는 비밀번호 불일치로 로그인 실패",
    schema: {
      example: {
        message: "로그인에 실패하였습니다.",
        error: "Unauthorized",
        statusCode: 401,
      },
    },
  })
  @Post("/login")
  @HttpCode(HttpStatus.OK)
  async login(@Body() dto: UserLoginDto): Promise<JSON> {
    this.logger.log("Post /api/auth/login");
    this.logger.verbose("User login dto: " + JSON.stringify(dto, null, 2));
    const result = await this.userApiService.login(dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "토큰 검증" })
  @ApiBearerAuth("access-token")
  @ApiUnauthorizedResponse({
    description: "유효한 토큰이 아닐 때",
    schema: {
      example: {
        message: "유효하지 않은 사용자입니다.",
        error: "Unauthorized",
        statusCode: 401,
      },
    },
  })
  @ApiOkResponse({
    description: "유효한 토큰일 때",
    schema: {
      example: {
        message: "토큰 검증 완료",
      },
    },
  })
  @UseGuards(AuthGuard)
  @Get("/verify")
  verify(): Promise<JSON> {
    this.logger.log("Get /api/auth/verify");
    const body: HttpResponse = {
      message: "토큰 검증 완료",
    };
    return JSON.parse(JSON.stringify(body));
  }

  @ApiOperation({ summary: "프로필 이미지 기본으로 설정" })
  @ApiBearerAuth("access-token")
  @ApiUnauthorizedResponse({
    description: "유효한 토큰이 아닐 때",
    schema: {
      example: {
        message: "유효하지 않은 사용자입니다.",
        error: "Unauthorized",
        statusCode: 401,
      },
    },
  })
  @ApiOkResponse({
    description: "기본 이미지로 변경 성공",
    schema: {
      example: {
        message: "프로필 이미지를 기본 이미지로 변경하였습니다.",
      },
    },
  })
  @UseGuards(AuthGuard)
  @Patch("/set-default-image")
  async setProfileImageDefault(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /api/auth/set-default-image");
    this.logger.verbose("Token: " + token);
    const result = await this.userApiService.setProfileImageDefault(token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "회원 탈퇴" })
  @ApiBearerAuth("access-token")
  @ApiUnauthorizedResponse({
    description: "유효한 토큰이 아닐 때",
    schema: {
      example: {
        message: "유효하지 않은 사용자입니다.",
        error: "Unauthorized",
        statusCode: 401,
      },
    },
  })
  @ApiOkResponse({
    description: "회원 탈퇴 완료",
    schema: {
      example: {
        message: "회원탈퇴 완료",
      },
    },
  })
  @UseGuards(AuthGuard)
  @Delete()
  async deleteUser(@Token() token: string): Promise<JSON> {
    this.logger.log("Delete /api/auth/");
    this.logger.verbose("Token: " + token);
    const result = await this.userApiService.delete(token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "사용자 정보 조회" })
  @ApiBearerAuth("access-token")
  @ApiUnauthorizedResponse({
    description: "유효한 토큰이 아닐 때",
    schema: {
      example: {
        message: "유효하지 않은 사용자입니다.",
        error: "Unauthorized",
        statusCode: 401,
      },
    },
  })
  @ApiOkResponse({
    description: "회원 정보 조회 성공",
    schema: {
      example: {
        message: "회원 정보 조회 성공",
        data: {
          nickname: "닉네임",
          email: "이메일",
          profileImage: "프로필이미지",
        },
      },
    },
  })
  @UseGuards(AuthGuard)
  @Get()
  async getUserInfo(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /api/auth/");
    this.logger.verbose("Token: " + token);
    const result = await this.userApiService.getUserInfo(token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "사용자 정보 업데이트" })
  @ApiConsumes("multipart/form-data")
  @ApiBearerAuth("access-token")
  @ApiUnauthorizedResponse({
    description: "유효한 토큰이 아닐 때",
    schema: {
      example: {
        message: "유효하지 않은 사용자입니다.",
        error: "Unauthorized",
        statusCode: 401,
      },
    },
  })
  @ApiOkResponse({
    description: "정보 수정 성공",
    schema: {
      example: {
        message: "정보 수정 성공",
      },
    },
  })
  @ApiUnprocessableEntityResponse({
    description: "사용자가 업로드한 이미지가 선정적이거나 폭력적인 사진일 때",
    schema: {
      example: {
        message: "서비스 규칙에 위배되는 사진입니다.",
        error: "Unprocessable Entity",
        statusCode: 422,
      },
    },
  })
  @UseGuards(AuthGuard)
  @Patch()
  @UseInterceptors(FileInterceptor("profileImage"))
  @HttpCode(HttpStatus.OK)
  async updateUserInfo(
    @Token() token: string,
    @UploadedFile() profileImage: Express.Multer.File,
    @Body("nickname") nickname: string,
  ): Promise<JSON> {
    this.logger.log("Patch /api/auth/");
    this.logger.verbose("nickname: " + nickname);
    this.logger.verbose("Image" + profileImage);
    nickname = nickname.replace(/"/g, "");
    const result = await this.userApiService.updateUserInfo(token, profileImage, nickname);
    return JSON.parse(result);
  }

  @Get("/logout")
  @ApiOperation({ summary: "로그아웃" })
  @ApiBearerAuth("access-token")
  @ApiOkResponse({
    description: "로그아웃 성공",
    schema: {
      example: {
        message: "로그아웃 성공",
      },
    },
  })
  @ApiUnauthorizedResponse({
    description: "유효한 토큰이 아닐 때",
    schema: {
      example: {
        message: "유효하지 않은 사용자입니다.",
        error: "Unauthorized",
        statusCode: 401,
      },
    },
  })
  @ApiOkResponse({})
  @UseGuards(AuthGuard)
  async logout(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /api/auth/logout");
    this.logger.verbose("Token: " + token);
    const result = await this.userApiService.logout(token);
    return JSON.parse(result);
  }
}
