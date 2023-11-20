import { Module } from "@nestjs/common";

import { ScheduleService } from "./schedule.service";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { ScheduleMetaRepository } from "./schedule.meta.repository";
import { ScheduleRepository } from "./schedule.repository";
import { ScheduleApiController } from "src/schedule-api/schedule-api.controller";
import { UserCheckRepository } from "./user.check.repository";
import { CategoryCheckRepository } from "./category.check.repository";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetaEntity])],
  controllers: [ScheduleApiController],
  providers: [
    ScheduleService,
    ScheduleMetaRepository,
    ScheduleRepository,
    UserCheckRepository,
    CategoryCheckRepository,
  ],
  exports: [ScheduleService],
})
export class ScheduleModule {}
