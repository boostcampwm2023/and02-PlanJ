import { Injectable, InternalServerErrorException } from "@nestjs/common";
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

    const from = await this.userService.getUserEntity(userUuid);
    const to = await this.userService.getUserEntityByEmail(dto.friendEmail);

    try {
      await this.friendRepository.add(from, to);
      const body: HttpResponse = {
        message: "친구가 되었습니다 ^^",
      };
      return JSON.stringify(body);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async getAllFriends(token: string) {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);

    try {
      const rawFriendList = await this.friendRepository.getAllFriends(userEntity);

      const friendList = await Promise.all(
        rawFriendList.map(async (friendEntity) => {
          const user = await this.userService.getUserEntityById(friendEntity.toId);
          return { userUuid: user.userUuid, email: user.email, nickname: user.nickname };
        }),
      );

      const body: HttpResponse = {
        message: "친구 목록 조회 성공",
        data: friendList,
      };
      return JSON.stringify(body);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }
}
