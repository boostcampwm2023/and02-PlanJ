import { Module } from "@nestjs/common";
import { FriendController } from "./friend.controller";
import { FriendService } from "./friend.service";
import { FriendRepository } from "./friend.repository";
import { TypeOrmModule } from "@nestjs/typeorm";
import { FriendsEntity } from "./entity/friends.entity";
import { UserService } from "src/user/user.service";
import { UserRepository } from "src/user/user.repository";

@Module({
  imports: [TypeOrmModule.forFeature([FriendsEntity])],
  controllers: [FriendController],
  providers: [FriendService, FriendRepository, UserService, UserRepository],
})
export class FriendModule {}
