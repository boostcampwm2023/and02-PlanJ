import { BadRequestException, Injectable, InternalServerErrorException } from "@nestjs/common";
import { FriendRepository } from "./friend.repository";
import { AddFriendDto } from "./dto/add-friend.dto";
import { UserService } from "src/user/user.service";
import { HttpResponse } from "src/utils/http.response";
import { AuthService } from "src/auth/auth.service";

@Injectable()
export class FriendService {
  constructor(
    private userService: UserService,
    private authService: AuthService,
    private friendRepository: FriendRepository,
  ) {}

  async add(token: string, dto: AddFriendDto): Promise<string> {
    const userUuid = this.authService.verify(token);

    if (userUuid !== dto.from) {
      throw new BadRequestException();
    }

    const from = await this.userService.getUserEntity(dto.from);
    const to = await this.userService.getUserEntity(dto.to);

    try {
      await this.friendRepository.add(from, to);
      const body: HttpResponse = {
        message: "친구가 되었습니다 ^^",
      };
      return JSON.stringify(body);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }
}
