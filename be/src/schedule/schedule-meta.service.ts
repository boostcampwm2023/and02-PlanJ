import { BadRequestException, Injectable, InternalServerErrorException, Logger } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaRepository } from "./schedule-meta.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleResponse } from "./dto/schedule.response";
import { ScheduleEntity } from "./entity/schedule.entity";

@Injectable()
export class ScheduleMetaService {
  private readonly logger = new Logger(ScheduleMetaService.name);
  constructor(
    @InjectRepository(ScheduleMetaRepository)
    private scheduleMetaRepository: ScheduleMetaRepository,
  ) {}

  async addScheduleMetadata(
    dto: AddScheduleDto,
    user: UserEntity,
    category: CategoryEntity = null,
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
      this.logger.error(error);
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

    if (!record) {
      throw new BadRequestException("해당하는 일정이 없습니다.");
    }

    if (startAt > endAt) {
      throw new BadRequestException("종료 시각이 시작 시각보다 빠릅니다.");
    }

    record.category = category;
    record.title = title;
    record.description = description;
    record.startTime = startTime;
    record.endTime = endTime;
    record.hasLocation = !!dto.endLocation;
    record.repeated = !!dto.repetition;
    record.hasAlarm = !!dto.alarm;

    try {
      await this.scheduleMetaRepository.save(record);
      return record;
    } catch (error) {
      this.logger.error(error);
      throw new InternalServerErrorException();
    }
  }

  async getAllScheduleByDate(user: UserEntity, date: Date): Promise<[ScheduleResponse[], ScheduleEntity[]]> {
    const rawSchedules = await this.scheduleMetaRepository.getAllScheduleByDate(user, date);
    return this.convertRawDataToResponse(rawSchedules);
  }

  async getAllScheduleByWeek(user: UserEntity, date: Date): Promise<[ScheduleResponse[], ScheduleEntity[]]> {
    const { firstDay, lastDay } = this.getWeekRange(date);
    const rawSchedules = await this.scheduleMetaRepository.getAllScheduleByWeek(user, firstDay, lastDay);
    return this.convertRawDataToResponse(rawSchedules);
  }

  async deleteScheduleMeta(metadataId: number): Promise<void> {
    await this.scheduleMetaRepository.deleteScheduleMeta(metadataId);
  }

  async getAllScheduleByCategoryId(categoryId: number, userId: number) {
    const rawSchedules = await this.scheduleMetaRepository.findByCategoryId(categoryId, userId);
    return this.convertRawDataToResponse(rawSchedules);
  }

  async getAllScheduleByNullCategory(userId: number) {
    const rawSchedules = await this.scheduleMetaRepository.findWhereCategoryIsNull(userId);
    return this.convertRawDataToResponse(rawSchedules);
  }

  async getAllScheduleByKeyword(keyword: string, userId: number) {
    const rawSchedules = await this.scheduleMetaRepository.findByKeyword(keyword, userId);
    return this.convertRawDataToResponse(rawSchedules);
  }

  private convertRawDataToResponse(rawSchedules: ScheduleMetadataEntity[]): [ScheduleResponse[], ScheduleEntity[]] {
    const updatedSchedules: ScheduleEntity[] = [];
    const scheduleResponses: ScheduleResponse[] = rawSchedules.flatMap((scheduleMeta) => {
      return scheduleMeta.children.map((schedule) => {
        if (!schedule.finished && !schedule.failed) {
          schedule.failed = this.checkFailed(schedule.endAt);

          if (schedule.failed) {
            updatedSchedules.push(schedule);
          }
        }
        const scheduleResponse: ScheduleResponse = {
          scheduleUuid: schedule.scheduleUuid,
          title: scheduleMeta.title,
          startAt: schedule.startAt ?? null,
          endAt: schedule.endAt,
          finished: schedule.finished,
          failed: schedule.failed,
          repeated: scheduleMeta.repeated,
          shared: scheduleMeta.shared,
          hasAlarm: scheduleMeta.hasAlarm,
          hasRetrospectiveMemo: !!schedule.retrospectiveMemo,
          participantCount: 0,
          participantSuccessCount: 0,
        };

        return scheduleResponse;
      });
    });

    return [scheduleResponses, updatedSchedules];
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

  async updateSharedStatus(metadataId: number) {
    const record = await this.scheduleMetaRepository.findOne({ where: { metadataId } });
    record.shared = true;
    await this.scheduleMetaRepository.save(record);
  }

  async getScheduleMetadataById(metadataId: number) {
    return await this.scheduleMetaRepository.findOne({ where: { metadataId } });
  }

  private checkFailed(endAt: string) {
    const endTime = new Date(endAt);
    const now = new Date();
    return endTime < now;
  }
}
