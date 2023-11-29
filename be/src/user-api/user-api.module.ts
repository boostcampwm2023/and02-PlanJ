import { Module } from "@nestjs/common";
import { UserApiController } from "./user-api.controller";
import { UserModule } from "src/user/user.module";
import { AuthModule } from "../auth/auth.module";
import { UserApiService } from "./user-api.service";
import { ImageModule } from "../image/image.module";

@Module({
  imports: [UserModule, AuthModule, ImageModule],
  providers: [UserApiService],
  controllers: [UserApiController],
})
export class UserApiModule {}
