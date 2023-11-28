import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleRepository } from "./schedule.repository";
import { ScheduleMetaService } from "./schedule-meta.service";
import { ScheduleService } from "./schedule.service";
import { ScheduleLocationService } from "./schedule-location.service";
import { RepetitionService } from "./repetition.service";
import { ParticipateService } from "./participate.service";
import { ParticipateRepository } from "./participate.repository";
import { ParticipantEntity } from "./entity/participant.entity";
import { RepetitionEntity } from "./entity/repetition.entity";
import { ScheduleLocationEntity } from "./entity/schedule-location.entity";

@Module({
  imports: [
    TypeOrmModule.forFeature([ScheduleMetadataEntity, ParticipantEntity, RepetitionEntity, ScheduleLocationEntity]),
  ],
  providers: [
    ScheduleMetaRepository,
    ScheduleRepository,
    ParticipateRepository,
    ScheduleMetaService,
    ScheduleService,
    ScheduleLocationService,
    RepetitionService,
    ParticipateService,
  ],
  exports: [ScheduleMetaService, ScheduleService, ScheduleLocationService, ParticipateService, RepetitionService],
})
export class ScheduleModule {}
