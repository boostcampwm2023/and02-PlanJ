import { Body, Controller, Post } from "@nestjs/common";
import { FriendService } from "./friend.service";
import { AddFriendDto } from "./dto/add-friend.dto";

@Controller("/api/friend")
export class FriendController {
  constructor(private friendService: FriendService) {}

  @Post("/add")
  async add(@Body() dto: AddFriendDto) {
    await this.friendService.add(dto);
  }
}
