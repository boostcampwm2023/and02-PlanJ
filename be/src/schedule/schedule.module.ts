import { Module } from "@nestjs/common";
import { ScheduleController } from "./schedule.controller";
import { ScheduleService } from "./schedule.service";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { ScheduleMetaRepository } from "./schedule.meta.repository";
import { ScheduleRepository } from "./schedule.repository";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetaEntity])],
  controllers: [ScheduleController],
  providers: [ScheduleService, ScheduleMetaRepository, ScheduleRepository],
})
export class ScheduleModule {}
