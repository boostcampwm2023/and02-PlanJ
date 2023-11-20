import { Module } from "@nestjs/common";
import { ScheduleApiController } from "./schedule-api.controller";
import { ScheduleModule } from "src/schedule/schedule.module";
import { ScheduleMetaDataModule } from "src/schedule-meta-data/schedule-meta-data.module";
import { AuthModule } from "src/auth/auth.module";
import { CategoryModule } from "src/category/category.module";

@Module({
  imports: [ScheduleModule, ScheduleMetaDataModule, CategoryModule, AuthModule],
  controllers: [ScheduleApiController],
  providers: [],
})
export class ScheduleApiModule {}
