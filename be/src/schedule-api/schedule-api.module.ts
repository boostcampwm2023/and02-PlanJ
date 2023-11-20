import { Module } from "@nestjs/common";
import { ScheduleApiController } from "./schedule-api.controller";
import { ScheduleModule } from "src/schedule/schedule.module";

@Module({
  imports: [ScheduleModule],
  controllers: [ScheduleApiController],
  providers: [],
})
export class ScheduleApiModule {}
