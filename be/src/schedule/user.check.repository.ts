import { Injectable } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { UserEntity } from "src/user/entity/user.entity";
import { AddScheduleDto } from "./dto/add-schedule.dto";

@Injectable()
export class UserCheckRepository extends Repository<UserEntity> {
  constructor(dataSource: DataSource) {
    super(UserEntity, dataSource.createEntityManager());
  }

  async checkByUserUuid(dto: AddScheduleDto): Promise<UserEntity> {
    const { userUuid } = dto;
    return await this.getUserEntity(userUuid);
  }

  private async getUserEntity(userUuid: string) {
    const user = await this.findOne({
      where: { userUuid: userUuid },
    });
    return user;
  }
}
