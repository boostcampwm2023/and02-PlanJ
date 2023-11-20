import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaRepository } from "./schedule.meta.repository";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";

@Injectable()
export class AddScheduleMetadataService {
  constructor(
    @InjectRepository(ScheduleMetaRepository)
    private scheduleMetaRepository: ScheduleMetaRepository,
  ) {}

  async addScheduleMetadata(
    dto: AddScheduleDto,
    user: UserEntity,
    category: CategoryEntity,
  ): Promise<ScheduleMetaEntity> {
    return await this.scheduleMetaRepository.addScheduleMeta(dto, user, category);
  }
}
