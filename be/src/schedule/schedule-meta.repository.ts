import { DataSource, Repository } from "typeorm";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class ScheduleMetaRepository extends Repository<ScheduleMetadataEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleMetadataEntity, dataSource.createEntityManager());
  }

  async getAllScheduleByDate(user: UserEntity, date: Date): Promise<ScheduleMetadataEntity[]> {
    const todayStart = date.toString().split("T")[0] + "T00:00:00";
    const todayEnd = date.toString().split("T")[0] + "T23:59:59";

    return await this.createQueryBuilder("schedule_metadata")
      .leftJoinAndSelect("schedule_metadata.children", "schedule", "schedule_metadata.user_id = :userId", {
        userId: user.userId,
      })
      .andWhere("schedule.endAt BETWEEN :todayStart AND :todayEnd ", { todayStart, todayEnd })
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
      console.log(error);
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
}
