import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { ulid } from "ulid";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";

@Injectable()
export class ScheduleMetaRepository extends Repository<ScheduleMetaEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleMetaEntity, dataSource.createEntityManager());
  }

  async addScheduleMeta(dto: AddScheduleDto, user: UserEntity, category: CategoryEntity): Promise<ScheduleMetaEntity> {
    const { categoryUuid, title, description, startAt, endAt } = dto;

    const startTime = startAt.split("T")[1];
    const endTime = endAt.split("T")[1];

    const scheduleMetadata = this.create({ title, description, startTime, endTime, user, category });

    try {
      await this.save(scheduleMetadata);
      return scheduleMetadata;
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }

  async getAllScheduleByDate(user: UserEntity, date: Date): Promise<ScheduleMetaEntity[]> {
    const todayStart = date.toString().split("T")[0] + "T00:00:00";
    const todayEnd = date.toString().split("T")[0] + "T23:59:59";

    const founds = await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule")
      .andWhere("schedule_metadata.user_id = :userId", { userId: user.userId })
      .andWhere("schedule.endAt BETWEEN :todayStart AND :todayEnd ", { todayStart, todayEnd })
      .getMany();

    return founds;
  }

  async getAllScheduleByWeek(user: UserEntity, firstDay: Date, lastDay: Date): Promise<ScheduleMetaEntity[]> {
    console.log(firstDay, lastDay);

    const weekStart = firstDay.toISOString().split("T")[0] + "T00:00:00";
    const weekEnd = lastDay.toISOString().split("T")[0] + "T23:59:59";

    const founds = await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule")
      .andWhere("schedule_metadata.user_id = :userId", { userId: user.userId })
      .andWhere("schedule.endAt BETWEEN :weekStart AND :weekEnd ", { weekStart, weekEnd })
      .getMany();

    return founds;
  }
}
