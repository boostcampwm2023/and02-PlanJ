import { Module } from "@nestjs/common";
import { AuthApiController } from "./auth-api.controller";
import { UserModule } from "src/user/user.module";
import {AuthModule} from "../auth/auth.module";
import {AuthApiService} from "./auth-api.service";

@Module({
  imports: [UserModule, AuthModule],
  providers: [AuthApiService],
  controllers: [AuthApiController],
})
export class AuthApiModule {}
