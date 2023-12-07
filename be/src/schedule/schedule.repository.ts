import { DataSource, MoreThan, Repository } from "typeorm";
import { Injectable } from "@nestjs/common";
import { ScheduleEntity } from "./entity/schedule.entity";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";

@Injectable()
export class ScheduleRepository extends Repository<ScheduleEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleEntity, dataSource.createEntityManager());
  }
  async getMetadataIdByScheduleUuid(scheduleUuid: string): Promise<number> {
    const record = await this.findOne({ where: { scheduleUuid } });
    return record.metadataId;
  }

  async deleteSchedule(dto: DeleteScheduleDto) {
    const { scheduleUuid } = dto;

    const record = await this.findOne({ where: { scheduleUuid } });
    await this.softDelete({ scheduleUuid });

    return record.metadataId;
  }

  async removeRepeatedSchedules(record: ScheduleEntity) {
    const { metadataId, endAt } = record;
    await this.softDelete({
      metadataId: metadataId,
      endAt: MoreThan(endAt),
    });
  }
}
