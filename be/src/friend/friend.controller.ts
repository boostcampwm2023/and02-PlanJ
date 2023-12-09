import { Body, Controller, Delete, Get, Logger, Post, UseGuards } from "@nestjs/common";
import { FriendService } from "./friend.service";
import { AddFriendDto } from "./dto/add-friend.dto";
import { AuthGuard } from "src/guard/auth.guard";
import { Token } from "src/utils/token.decorator";
import {
  ApiBadRequestResponse,
  ApiBearerAuth,
  ApiCreatedResponse,
  ApiOkResponse,
  ApiOperation,
  ApiTags,
  ApiUnauthorizedResponse,
} from "@nestjs/swagger";

@ApiTags("친구")
@Controller("/api/friend")
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
@UseGuards(AuthGuard)
export class FriendController {
  private readonly logger = new Logger(FriendController.name);
  constructor(private friendService: FriendService) {}

  @ApiOperation({ summary: "친구 추가" })
  @ApiCreatedResponse({
    description: "성공 메시지",
    schema: {
      example: {
        message: "친구가 되었습니다",
      },
    },
  })
  @ApiBadRequestResponse({
    description: "추가하려는 사용자가 존재하지 않을 때",
    schema: {
      example: {
        message: "존재하지 않는 사용자입니다.",
        error: "Bad Request",
        statusCode: 400,
      },
    },
  })
  @Post("/add")
  async add(@Token() token: string, @Body() dto: AddFriendDto): Promise<JSON> {
    this.logger.log("Post /api/friend/add");
    this.logger.verbose("Data: " + JSON.stringify(dto, null, 2));
    const result = await this.friendService.add(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "친구 목록 조회" })
  @ApiOkResponse({
    description: "사용자의 친구 목록",
    schema: {
      example: {
        message: "친구 목록 조회 성공",
        data: [
          {
            profileUrl: "url",
            email: "ccc@test.com",
            nickname: "ccc",
          },
        ],
      },
    },
  })
  @Get("/")
  async getAllFriends(@Token() token: string) {
    this.logger.log("Get /api/friend/");
    this.logger.verbose("Token: " + token);
    const result = await this.friendService.getAllFriends(token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "친구 삭제" })
  @ApiOkResponse({
    description: "친구 삭제 응답",
    schema: {
      example: {
        message: "친구 삭제 완료",
      },
    },
  })
  @Delete()
  async deleteFriend(@Token() token: string, @Body("email") email: string): Promise<JSON> {
    this.logger.log("Delete /api/friend/");
    this.logger.verbose("Token: " + token);
    this.logger.verbose("Email: " + email);
    const result = await this.friendService.deleteFriend(token, email);
    return JSON.parse(result);
  }
}
