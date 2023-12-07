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
import { ScheduleAlarmService } from "./schedule-alarm.service";
import { ScheduleAlarmEntity } from "./entity/schedule-alarm.entity";

@Module({
  imports: [
    TypeOrmModule.forFeature([
      ScheduleMetadataEntity,
      ParticipantEntity,
      RepetitionEntity,
      ScheduleLocationEntity,
      ScheduleAlarmEntity,
    ]),
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
    ScheduleAlarmService,
  ],
  exports: [
    ScheduleMetaService,
    ScheduleService,
    ScheduleLocationService,
    ParticipateService,
    RepetitionService,
    ScheduleAlarmService,
  ],
})
export class ScheduleModule {}
