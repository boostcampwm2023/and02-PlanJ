import { Module } from "@nestjs/common";
import { UserApiController } from "./user-api.controller";
import { UserModule } from "src/user/user.module";
import { AuthModule } from "../auth/auth.module";
import { UserApiService } from "./user-api.service";

@Module({
  imports: [UserModule, AuthModule],
  providers: [UserApiService],
  controllers: [UserApiController],
})
export class UserApiModule {}
