import { Module } from "@nestjs/common";
import { CategoryApiController } from "./category-api.controller";
import { ScheduleModule } from "src/schedule/schedule.module";
import { AuthModule } from "src/auth/auth.module";

@Module({
  imports: [ScheduleModule, AuthModule],
  controllers: [CategoryApiController],
})
export class CategoryApiModule {}
