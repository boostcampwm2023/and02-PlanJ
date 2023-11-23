import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";

@Injectable()
export class ScheduleMetaRepository extends Repository<ScheduleMetaEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleMetaEntity, dataSource.createEntityManager());
  }

  async addScheduleMeta(dto: AddScheduleDto, user: UserEntity, category: CategoryEntity): Promise<ScheduleMetaEntity> {
    const { title, endAt } = dto;

    const description = null;
    const startTime = null;
    const [, endTime] = endAt.split("T");

    const scheduleMetadata = this.create({ title, description, startTime, endTime, user, category });

    try {
      await this.save(scheduleMetadata);
      return scheduleMetadata;
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async updateScheduleMeta(
    dto: UpdateScheduleDto,
    category: CategoryEntity,
    metadataId: number,
  ): Promise<ScheduleMetaEntity> {
    const { title, description, startAt, endAt } = dto;

    const [, startTime] = startAt.split("T");
    const [, endTime] = endAt.split("T");

    const record = await this.findOne({ where: { metadataId } });
    record.category = category;
    record.title = title;
    record.description = description;
    record.startTime = startTime;
    record.endTime = endTime;

    try {
      await this.save(record);
      return record;
    } catch (error) {
      console.log(error);
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

  async deleteScheduleMeta(metadataId: number): Promise<void> {
    try {
      await this.softDelete({ metadataId });
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }
}
