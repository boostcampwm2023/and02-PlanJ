import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { ulid } from "ulid";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class ScheduleMetaRepository extends Repository<ScheduleMetaEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleMetaEntity, dataSource.createEntityManager());
  }

  async addScheduleMeta(dto: AddScheduleDto, user: UserEntity): Promise<ScheduleMetaEntity> {
    const { title, description, startAt, endAt } = dto;

    const startTime = startAt.split("T")[1];
    const endTime = endAt.split("T")[1];

    const scheduleMetadata = this.create({ title, description, startTime, endTime, user });

    try {
      await this.save(scheduleMetadata);
      return scheduleMetadata;
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }
}
