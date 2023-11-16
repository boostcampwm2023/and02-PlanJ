import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";

export class ScheduleRepository extends Repository<ScheduleMetaEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleMetaEntity, dataSource.createEntityManager());
  }

  addSchedule(dto: AddScheduleDto) {
    const { userId } = dto;

    const schedule = this.create({});
    return dto;
  }
}
