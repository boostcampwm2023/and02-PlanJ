import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";
import { HttpResponse } from "src/utils/http.response";

@Injectable()
export class ScheduleMetaService {
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

  async getAllScheduleByDate(user: UserEntity, date: Date): Promise<string> {
    const rawSchedules = await this.scheduleMetaRepository.getAllScheduleByDate(user, date);

    const schedules = rawSchedules.flatMap((scheduleMeta) => {
      return scheduleMeta.children.map((schedule) => ({
        scheduleUuid: schedule.scheduleUuid,
        title: scheduleMeta.title,
        description: scheduleMeta.description,
        startAt: schedule.startAt,
        endAt: schedule.endAt,
        finished: schedule.finished,
        failed: schedule.failed,
        remindMemo: schedule.remindMemo,
      }));
    });

    const body: HttpResponse = {
      message: "하루 일정 조회 성공",
      statusCode: 200,
      data: schedules,
    };

    return JSON.stringify(body);
  }
}
