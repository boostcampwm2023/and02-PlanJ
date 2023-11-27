import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { ulid } from "ulid";
import { ScheduleEntity } from "./entity/schedule.entity";
import { HttpResponse } from "src/utils/http.response";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";

@Injectable()
export class ScheduleRepository extends Repository<ScheduleEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleEntity, dataSource.createEntityManager());
  }

  async addSchedule(dto: AddScheduleDto, scheduleMetadata: ScheduleMetadataEntity) {
    const { endAt } = dto;

    const scheduleUuid = ulid();

    const schedule = this.create({
      scheduleUuid,
      startAt: null,
      endAt,
      finished: false,
      failed: false,
      remindMemo: "",
      last: true,
      parent: scheduleMetadata,
    });

    try {
      await this.save(schedule);
      const body: HttpResponse = {
        message: "일정 추가 성공",
        data: {
          scheduleUuid: scheduleUuid,
        },
      };

      return JSON.stringify(body);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async getMetadataIdByScheduleUuid(scheduleUuid: string): Promise<number> {
    const record = await this.findOne({ where: { scheduleUuid }, relations: ["parent"] });
    return record.parent.metadataId;
  }

  async updateSchedule(dto: UpdateScheduleDto): Promise<string> {
    const { scheduleUuid, startAt, endAt } = dto;
    const record = await this.findOne({ where: { scheduleUuid } });
    record.startAt = startAt;
    record.endAt = endAt;

    try {
      await this.save(record);
      const body: HttpResponse = {
        message: "일정 수정 성공",
        data: {
          scheduleUuid: scheduleUuid,
        },
      };

      return JSON.stringify(body);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async deleteSchedule(dto: DeleteScheduleDto) {
    const { scheduleUuid } = dto;

    const record = await this.findOne({ where: { scheduleUuid } });
    this.softDelete({ scheduleUuid });

    return record.metadataId;
  }
}
