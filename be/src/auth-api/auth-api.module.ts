import { Module } from "@nestjs/common";
import { AuthApiController } from "./auth-api.controller";
import { UserModule } from "src/user/user.module";

@Module({
  imports: [UserModule],
  controllers: [AuthApiController],
})
export class AuthApiModule {}
