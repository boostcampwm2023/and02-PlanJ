import { BadRequestException, Injectable, InternalServerErrorException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";
import { HttpResponse } from "src/utils/http.response";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleResponse } from "./dto/schedule.response";

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
  ): Promise<ScheduleMetadataEntity> {
    const { title, endAt } = dto;

    const description = null;
    const startTime = null;
    const [, endTime] = endAt.split("T");

    const scheduleMetadata = this.scheduleMetaRepository.create({
      title,
      description,
      startTime,
      endTime,
      category,
      user,
    });

    try {
      await this.scheduleMetaRepository.save(scheduleMetadata);
      return scheduleMetadata;
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async updateScheduleMetadata(
    dto: UpdateScheduleDto,
    category: CategoryEntity,
    metadataId: number,
  ): Promise<ScheduleMetadataEntity> {
    const { title, description, startAt, endAt } = dto;

    const [, startTime] = !!startAt ? startAt.split("T") : [null, null];
    const [, endTime] = endAt.split("T");

    const record = await this.scheduleMetaRepository.findOne({ where: { metadataId } });

    // if (!!record) {
    //   throw new BadRequestException("해당하는 일정이 없습니다.");
    // }

    record.category = category;
    record.title = title;
    record.description = description;
    record.startTime = startTime;
    record.endTime = endTime;

    try {
      await this.scheduleMetaRepository.save(record);
      return record;
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }

  async getAllScheduleByDate(user: UserEntity, date: Date): Promise<string> {
    const rawSchedules = await this.scheduleMetaRepository.getAllScheduleByDate(user, date);
    const schedules = this.convertRawDataToResponse(rawSchedules);

    const body: HttpResponse = {
      message: "하루 일정 조회 성공",
      data: schedules,
    };

    return JSON.stringify(body);
  }

  async getAllScheduleByWeek(user: UserEntity, date: Date): Promise<string> {
    const { firstDay, lastDay } = this.getWeekRange(date);
    const rawSchedules = await this.scheduleMetaRepository.getAllScheduleByWeek(user, firstDay, lastDay);
    const schedules = this.convertRawDataToResponse(rawSchedules);

    const body: HttpResponse = {
      message: "주간 일정 조회 성공",
      data: schedules,
    };

    return JSON.stringify(body);
  }

  async deleteScheduleMeta(metadataId: number): Promise<string> {
    await this.scheduleMetaRepository.deleteScheduleMeta(metadataId);

    const body: HttpResponse = {
      message: "일정 삭제 성공",
    };

    return JSON.stringify(body);
  }

  async getAllScheduleByCategoryId(categoryId: number, userId: number) {
    const rawSchedules = await this.scheduleMetaRepository.findByCategoryId(categoryId, userId);
    return this.convertRawDataToResponse(rawSchedules);
  }

  async getAllScheduleByNullCategory(userId: number) {
    const rawSchedules = await this.scheduleMetaRepository.findWhereCategoryIsNull(userId);
    return this.convertRawDataToResponse(rawSchedules);
  }

  private convertRawDataToResponse(rawSchedules: ScheduleMetadataEntity[]) {
    return rawSchedules.flatMap((scheduleMeta) => {
      return scheduleMeta.children.map((schedule) => {
        const scheduleResponse: ScheduleResponse = {
          scheduleUuid: schedule.scheduleUuid,
          title: scheduleMeta.title,
          description: scheduleMeta.description,
          startAt: schedule.startAt === null ? null : schedule.startAt,
          endAt: schedule.endAt,
          finished: schedule.finished,
          failed: schedule.failed, // TODO: 호출 시점에서 실패 처리
          remindMemo: schedule.remindMemo,
        };

        return scheduleResponse;
      });
    });
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
}
