import { Module } from "@nestjs/common";
import { ScheduleApiController } from "./schedule-api.controller";
import { ScheduleModule } from "src/schedule/schedule.module";
import { UserModule } from "src/user/user.module";
import { CategoryModule } from "src/category/category.module";
import { ScheduleApiService } from "./schedule-api.service";

@Module({
  imports: [ScheduleModule, CategoryModule, UserModule],
  controllers: [ScheduleApiController],
  providers: [ScheduleApiService],
})
export class ScheduleApiModule {}
