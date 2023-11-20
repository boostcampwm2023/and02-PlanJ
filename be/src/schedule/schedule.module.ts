import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { ScheduleMetaRepository } from "./schedule.meta.repository";
import { ScheduleRepository } from "./schedule.repository";
import { ScheduleApiController } from "src/schedule-api/schedule-api.controller";
import { UserCheckRepository } from "./user.check.repository";
import { CategoryCheckRepository } from "./category.check.repository";
import { GetUserEntityService } from "./get-user-entity.service";
import { GetCategoryEntityService } from "./get-category-entity.service";
import { AddScheduleMetadataService } from "./add-schedule-metadata.service";
import { AddScheduleService } from "./add-schedule.service";
import { ScheduleApiService } from "src/schedule-api/schedule-api.service";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetaEntity])],
  controllers: [ScheduleApiController],
  providers: [
    UserCheckRepository,
    CategoryCheckRepository,
    ScheduleMetaRepository,
    ScheduleRepository,
    ScheduleApiService,
    GetUserEntityService,
    GetCategoryEntityService,
    AddScheduleMetadataService,
    AddScheduleService,
  ],
  exports: [GetUserEntityService, GetCategoryEntityService, AddScheduleMetadataService, AddScheduleService],
})
export class ScheduleModule {}
