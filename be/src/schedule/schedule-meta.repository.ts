import { DataSource, Repository } from "typeorm";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { Injectable, InternalServerErrorException, Logger } from "@nestjs/common";
import { UserEntity } from "src/user/entity/user.entity";
import { MemoResponse } from "./dto/memo.response";
import { add } from "date-fns";
import { AlarmScheduleResponse } from "./dto/alarm-schedule.response";

@Injectable()
export class ScheduleMetaRepository extends Repository<ScheduleMetadataEntity> {
  private readonly logger = new Logger(ScheduleMetaRepository.name);
  constructor(dataSource: DataSource) {
    super(ScheduleMetadataEntity, dataSource.createEntityManager());
  }

  async getAllScheduleByDate(user: UserEntity, date: Date): Promise<ScheduleMetadataEntity[]> {
    const [today] = date.toString().split("T");
    const todayStart = today + "T00:00:00";
    const todayEnd = today + "T23:59:59";

    return await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule", "schedule_metadata.user_id = :userId", {
        userId: user.userId,
      })
      .andWhere("schedule.endAt BETWEEN :todayStart AND :todayEnd ", { todayStart, todayEnd })
      .orWhere(":todayEnd >= schedule.startAt AND :todayStart <= schedule.endAt", { todayEnd, todayStart })
      .orderBy("schedule.endAt")
      .getMany();
  }

  async getAllScheduleByWeek(user: UserEntity, firstDay: Date, lastDay: Date): Promise<ScheduleMetadataEntity[]> {
    const weekStart = firstDay.toISOString().split("T")[0] + "T00:00:00";
    const weekEnd = lastDay.toISOString().split("T")[0] + "T23:59:59";

    return await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule", "schedule_metadata.user_id = :userId", {
        userId: user.userId,
      })
      .andWhere(
        "(schedule.endAt BETWEEN :weekStart AND :weekEnd) OR (schedule.startAt BETWEEN :weekStart AND :weekEnd)",
        { weekStart, weekEnd },
      )
      .orderBy("schedule.endAt")
      .getMany();
  }

  async findByCategoryId(categoryId: number, userId: number) {
    return await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule")
      .andWhere("schedule_metadata.user_id = :userId", { userId: userId })
      .andWhere("schedule_metadata.category_id = :categoryId", { categoryId: categoryId })
      .orderBy("schedule.endAt")
      .getMany();
  }

  async deleteScheduleMeta(metadataId: number): Promise<void> {
    try {
      await this.softDelete({ metadataId });
    } catch (error) {
      this.logger.error(error);
      throw new InternalServerErrorException();
    }
  }

  async findWhereCategoryIsNull(userId: number) {
    return await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule")
      .where("schedule_metadata.user_id = :userId", { userId: userId })
      .andWhere("schedule_metadata.category_id IS NULL")
      .orderBy("schedule.endAt")
      .getMany();
  }

  async findByKeyword(keyword: string, userId: number) {
    return await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule")
      .where("schedule_metadata.user_id = :userId", { userId: userId })
      .andWhere("schedule_metadata.title LIKE :keyword", { keyword: `%${keyword}%` })
      .getMany();
  }

  async findByUserId(userId: number) {
    try {
      const result: MemoResponse[] = await this.query(
        `SELECT meta.title as title, s.start_at as startAt, s.end_at as endAt, s.retrospective_memo as retrospectiveMemo
             FROM schedule_metadata as meta LEFT OUTER JOIN schedule as s
             ON meta.id = s.metadata_id
             WHERE meta.user_id=?
             AND s.retrospective_memo IS NOT NULL
             ORDER BY s.end_at DESC`,
        [userId],
      );
      return result;
    } catch (e) {
      this.logger.error(e);
      throw e;
    }
  }

  async findHasAlarm(userId: number) {
    const today = add(new Date(), { hours: 9 });
    const threeDaysAfter = add(today, { days: 3 });
    const [todayString] = today.toISOString().split("T");
    const [endDayString] = threeDaysAfter.toISOString().split("T");

    try {
      const result: AlarmScheduleResponse[] = await this.query(
        `SELECT meta.title AS title, s.end_at AS endAt, s.uuid AS scheduleUuid, a.alarm_time AS alarmTime,
             a.alarm_type AS alarmType, a.estimated_time AS estimatedTime
             FROM schedule_metadata AS meta 
             LEFT OUTER JOIN schedule AS s ON meta.id = s.metadata_id
             LEFT OUTER JOIN schedule_alarm AS a ON meta.id = a.metadata_id
             WHERE meta.user_id=?
             AND meta.deleted_at IS NULL
             AND s.deleted_at IS NULL
             AND meta.has_alarm = true
             AND s.end_at BETWEEN ? AND ?`,
        [userId, todayString, endDayString],
      );
      return result;
    } catch (e) {
      this.logger.error(e);
      throw e;
    }
  }
}
