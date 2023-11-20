import { Module } from "@nestjs/common";
import { AuthService } from "./auth.service";
import { UserEntity } from "./entity/user.entity";
import { TypeOrmModule } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { AuthApiController } from "src/auth-api/auth-api.controller";

@Module({
  imports: [TypeOrmModule.forFeature([UserEntity])],
  controllers: [AuthApiController],
  providers: [AuthService, UserRepository],
  exports: [AuthService],
})
export class AuthModule {}
