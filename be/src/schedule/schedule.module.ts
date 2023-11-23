import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleRepository } from "./schedule.repository";
import { ScheduleApiController } from "src/schedule-api/schedule-api.controller";
import { ScheduleMetaService } from "./schedule-meta.service";
import { ScheduleService } from "./schedule.service";
import { ScheduleApiService } from "src/schedule-api/schedule-api.service";
import { UserRepository } from "src/user/user.repository";
import { CategoryRepository } from "src/category/category.repository";
import { UserService } from "src/user/user.service";
import { CategoryService } from "src/category/category.service";
import { ScheduleLocationService } from "./schedule-location.service";
import { ScheduleLocationRepository } from "./schedule-location.respository";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetaEntity])],
  controllers: [ScheduleApiController],
  providers: [
    UserRepository,
    CategoryRepository,
    ScheduleMetaRepository,
    ScheduleRepository,
    ScheduleLocationRepository,
    ScheduleApiService,
    UserService,
    CategoryService,
    ScheduleMetaService,
    ScheduleService,
    ScheduleLocationService,
  ],
  exports: [ScheduleMetaService, ScheduleService, ScheduleLocationService],
})
export class ScheduleModule {}
