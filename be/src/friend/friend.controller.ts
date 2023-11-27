import { Body, Controller, Get, Post, UseGuards } from "@nestjs/common";
import { FriendService } from "./friend.service";
import { AddFriendDto } from "./dto/add-friend.dto";
import { AuthGuard } from "src/guard/auth.guard";
import { Token } from "src/utils/token.decorator";

@Controller("/api/friend")
@UseGuards(AuthGuard)
export class FriendController {
  constructor(private friendService: FriendService) {}

  @Post("/add")
  async add(@Token() token: string, @Body() dto: AddFriendDto): Promise<JSON> {
    const result = await this.friendService.add(token, dto);
    return JSON.parse(result);
  }

  @Get("/")
  async getAllFriends(@Token() token: string) {
    const result = await this.friendService.getAllFriends(token);
    return JSON.parse(result);
  }
}
