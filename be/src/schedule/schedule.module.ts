import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleRepository } from "./schedule.repository";
import { ScheduleMetaService } from "./schedule-meta.service";
import { ScheduleService } from "./schedule.service";
import { ScheduleLocationService } from "./schedule-location.service";
import { ScheduleLocationRepository } from "./schedule-location.repository";
import { ParticipateService } from "./participate.service";
import { ParticipateRepository } from "./participate.repository";
import { ParticipantEntity } from "./entity/participant.entity";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetadataEntity, ParticipantEntity])],
  providers: [
    ScheduleMetaRepository,
    ScheduleRepository,
    ScheduleLocationRepository,
    ParticipateRepository,
    ScheduleMetaService,
    ScheduleService,
    ScheduleLocationService,
    ParticipateService,
  ],
  exports: [ScheduleMetaService, ScheduleService, ScheduleLocationService, ParticipateService],
})
export class ScheduleModule {}
