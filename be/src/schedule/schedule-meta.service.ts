import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";
import { HttpResponse } from "src/utils/http.response";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";

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

  async updateScheduleMetadata(
    dto: UpdateScheduleDto,
    category: CategoryEntity,
    metadataId: number,
  ): Promise<ScheduleMetaEntity> {
    return await this.scheduleMetaRepository.updateScheduleMeta(dto, category, metadataId);
  }

  async getAllScheduleByDate(user: UserEntity, date: Date): Promise<string> {
    const rawSchedules = await this.scheduleMetaRepository.getAllScheduleByDate(user, date);

    const schedules = rawSchedules.flatMap((scheduleMeta) => {
      return scheduleMeta.children.map((schedule) => ({
        scheduleUuid: schedule.scheduleUuid,
        title: scheduleMeta.title,
        description: scheduleMeta.description,
        startAt: schedule.startAt === null ? null : schedule.startAt.slice(0, -5),
        endAt: schedule.endAt.slice(0, -5),
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

  async getAllScheduleByWeek(user: UserEntity, date: Date): Promise<string> {
    const { firstDay, lastDay } = this.getWeekRange(date);

    const rawSchedules = await this.scheduleMetaRepository.getAllScheduleByWeek(user, firstDay, lastDay);

    const schedules = rawSchedules.flatMap((scheduleMeta) => {
      return scheduleMeta.children.map((schedule) => ({
        scheduleUuid: schedule.scheduleUuid,
        title: scheduleMeta.title,
        description: scheduleMeta.description,
        startAt: schedule.startAt === null ? null : schedule.startAt.slice(0, -5),
        endAt: schedule.endAt.slice(0, -5),
        finished: schedule.finished,
        failed: schedule.failed,
        remindMemo: schedule.remindMemo,
      }));
    });

    const body: HttpResponse = {
      message: "주간 일정 조회 성공",
      statusCode: 200,
      data: schedules,
    };

    return JSON.stringify(body);
  }

  private getWeekRange(date: Date) {
    const dateObj = new Date(date);
    const firstDay = new Date(date);
    firstDay.setDate(dateObj.getDate() - dateObj.getDay());
    firstDay.toISOString();

    const lastDay = new Date(date);
    lastDay.setDate(dateObj.getDate() + (6 - dateObj.getDay()));
    lastDay.toISOString();

    return { firstDay, lastDay };
  }

  async deleteScheduleMeta(metadataId: number): Promise<string> {
    await this.scheduleMetaRepository.deleteScheduleMeta(metadataId);

    const body: HttpResponse = {
      message: "일정 삭제 성공",
      statusCode: 200,
    };

    return JSON.stringify(body);
  }
}
