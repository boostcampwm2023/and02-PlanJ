import { Module } from "@nestjs/common";
import { CategoryApiController } from "./category-api.controller";
import { ScheduleModule } from "src/schedule/schedule.module";
import { UserModule } from "src/user/user.module";
import { CategoryModule } from "src/category/category.module";
import { CategoryApiService } from "./category-api.service";
import { AuthModule } from "../auth/auth.module";

@Module({
  imports: [ScheduleModule, CategoryModule, UserModule, AuthModule],
  controllers: [CategoryApiController],
  providers: [CategoryApiService],
})
export class CategoryApiModule {}
