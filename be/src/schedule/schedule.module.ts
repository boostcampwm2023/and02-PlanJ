import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleRepository } from "./schedule.repository";
import { ScheduleMetaService } from "./schedule-meta.service";
import { ScheduleService } from "./schedule.service";
import { ScheduleLocationService } from "./schedule-location.service";
import { ScheduleLocationRepository } from "./schedule-location.repository";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetadataEntity])],
  providers: [
    ScheduleMetaRepository,
    ScheduleRepository,
    ScheduleLocationRepository,
    ScheduleMetaService,
    ScheduleService,
    ScheduleLocationService,
  ],
  exports: [ScheduleMetaService, ScheduleService, ScheduleLocationService],
})
export class ScheduleModule {}
