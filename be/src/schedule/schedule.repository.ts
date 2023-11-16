import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { HttpResponse } from "src/utils/http.response";
import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { ulid } from "ulid";

@Injectable()
export class ScheduleRepository extends Repository<ScheduleMetaEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleMetaEntity, dataSource.createEntityManager());
  }

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    const { userId, title, description, startTime, endTime } = dto;

    const metaId = ulid();
    const schedule = this.create({ metaId, title, description, startTime, endTime });

    console.log("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
    console.log(schedule);
    console.log("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");

    try {
      console.log("ㅌㅌㅌㅌㅌㅌㅌㅌㅌㅌㅌㅌㅌㅌㅌ");
      await this.save(schedule);
      const body: HttpResponse = {
        message: "일정 추가 성공",
        statusCode: 200,
        data: {
          pk: metaId,
        },
      };

      console.log(body);

      return JSON.stringify(body);
    } catch (error) {
      console.log(error.message);
      throw new InternalServerErrorException();
    }
  }
}
