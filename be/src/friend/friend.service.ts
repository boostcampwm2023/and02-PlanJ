import { BadRequestException, Injectable, InternalServerErrorException, Logger } from "@nestjs/common";
import { FriendRepository } from "./friend.repository";
import { AddFriendDto } from "./dto/add-friend.dto";
import { UserService } from "src/user/user.service";
import { HttpResponse } from "src/utils/http.response";
import { AuthService } from "src/auth/auth.service";
import { PushService } from "../push/push.service";

@Injectable()
export class FriendService {
  private readonly logger = new Logger(FriendService.name);
  constructor(
    private userService: UserService,
    private authService: AuthService,
    private friendRepository: FriendRepository,
    private pushService: PushService,
  ) {}

  async add(token: string, dto: AddFriendDto): Promise<string> {
    const userUuid = this.authService.verify(token);

    const from = await this.userService.getUserEntity(userUuid);
    const to = await this.userService.getUserEntityByEmail(dto.friendEmail);

    if (!to) {
      // 상대가 존재하지 않는 사용자일 때
      throw new BadRequestException("존재하지 않는 사용자입니다.");
    }

    const exist = await this.friendRepository.findOne({ where: { fromId: from.userId, toId: to.userId } });
    if (exist) {
      //이미 친구일 경우
      const body: HttpResponse = {
        message: "이미 친구인 사용자입니다.",
      };
      return JSON.stringify(body);
    }

    try {
      await this.friendRepository.add(from, to);

      if (!!to.deviceToken) {
        const message = `${from.nickname}님과 친구가 되었습니다.`;
        this.pushService.sendPush(to.deviceToken, message);
      }

      const body: HttpResponse = {
        message: "친구가 되었습니다",
      };
      return JSON.stringify(body);
    } catch (error) {
      this.logger.error(error);
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
          return { profileUrl: user.profileUrl, email: user.email, nickname: user.nickname };
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

  async deleteFriend(token: string, email: string) {
    const userUuid = this.authService.verify(token);
    const [userEntity, friendEntity] = await Promise.all([
      this.userService.getUserEntity(userUuid),
      this.userService.getUserEntityByEmail(email),
    ]);

    const userId = userEntity.userId;
    const friendId = friendEntity.userId;

    await Promise.all([
      this.friendRepository.softDelete({
        fromId: userId,
        toId: friendId,
      }),
      this.friendRepository.softDelete({
        fromId: friendId,
        toId: userId,
      }),
    ]);

    const body: HttpResponse = {
      message: "친구 삭제 완료",
    };
    return JSON.stringify(body);
  }
}
