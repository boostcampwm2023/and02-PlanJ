import { HttpResponse } from "./../utils/http.response";
import { Module } from "@nestjs/common";
import { ScheduleController } from "./schedule.controller";
import { ScheduleService } from "./schedule.service";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { ScheduleRepository } from "./schedule.repository";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetaEntity])],
  controllers: [ScheduleController],
  providers: [ScheduleService, ScheduleRepository],
})
export class ScheduleModule {}
