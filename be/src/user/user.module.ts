import { Module } from "@nestjs/common";
import { UserService } from "./user.service";
import { UserEntity } from "./entity/user.entity";
import { TypeOrmModule } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { UserApiController } from "src/user-api/user-api.controller";

@Module({
  imports: [TypeOrmModule.forFeature([UserEntity])],
  providers: [UserService, UserRepository],
  exports: [UserService],
})
export class UserModule {}
