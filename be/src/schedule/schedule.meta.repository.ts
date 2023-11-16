import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { HttpResponse } from "src/utils/http.response";
import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { ulid } from "ulid";

@Injectable()
export class ScheduleMetaRepository extends Repository<ScheduleMetaEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleMetaEntity, dataSource.createEntityManager());
  }

  async addScheduleMeta(dto: AddScheduleDto): Promise<void> {
    const { userId, title, description, startAt, endAt } = dto;

    const startTime = startAt.split("T")[1];
    const endTime = endAt.split("T")[1];

    const metaId = ulid();
    const schedule = this.create({ metaId, title, description, startTime, endTime });

    try {
      await this.save(schedule);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }
}
