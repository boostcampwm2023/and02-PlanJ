import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleRepository } from "./schedule.repository";
import { ScheduleApiController } from "src/schedule-api/schedule-api.controller";
import { ScheduleMetadataService } from "./schedule-meta.service";
import { ScheduleService } from "./schedule.service";
import { ScheduleApiService } from "src/schedule-api/schedule-api.service";
import { UserRepository } from "src/user/user.repository";
import { CategoryRepository } from "src/category/category.repository";
import { UserService } from "src/user/user.service";
import { CategoryService } from "src/category/category.service";

@Module({
  imports: [TypeOrmModule.forFeature([ScheduleMetaEntity])],
  controllers: [ScheduleApiController],
  providers: [
    UserRepository,
    CategoryRepository,
    ScheduleMetaRepository,
    ScheduleRepository,
    ScheduleApiService,
    UserService,
    CategoryService,
    ScheduleMetadataService,
    ScheduleService,
  ],
  exports: [ScheduleMetadataService, ScheduleService],
})
export class ScheduleModule {}
