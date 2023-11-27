import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { FriendRepository } from "./friend.repository";
import { AddFriendDto } from "./dto/add-friend.dto";
import { UserService } from "src/user/user.service";

@Injectable()
export class FriendService {
  constructor(
    private userService: UserService,
    private friendRepository: FriendRepository,
  ) {}

  async add(dto: AddFriendDto) {
    const from = await this.userService.getUserEntity(dto.from);
    const to = await this.userService.getUserEntity(dto.to);

    try {
      await this.friendRepository.add(from, to);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }
}
