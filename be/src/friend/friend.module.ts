import { Module } from "@nestjs/common";
import { FriendController } from "./friend.controller";
import { FriendService } from "./friend.service";
import { FriendRepository } from "./friend.repository";
import { TypeOrmModule } from "@nestjs/typeorm";
import { FriendEntity } from "./entity/friend.entity";
import { UserService } from "src/user/user.service";
import { UserRepository } from "src/user/user.repository";
import { AuthModule } from "src/auth/auth.module";
import { PushModule } from "../push/push.module";

@Module({
  imports: [TypeOrmModule.forFeature([FriendEntity]), AuthModule, PushModule],
  controllers: [FriendController],
  providers: [FriendService, FriendRepository, UserService, UserRepository],
})
export class FriendModule {}
