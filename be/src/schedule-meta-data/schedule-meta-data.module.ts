import { Module } from "@nestjs/common";
import { ScheduleMetaDataService } from "./schedule-meta-data.service";

@Module({
  providers: [ScheduleMetaDataService],
})
export class ScheduleMetaDataModule {}
