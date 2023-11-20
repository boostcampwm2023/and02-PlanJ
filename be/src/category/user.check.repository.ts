import { Injectable } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { AddCategoryDto } from "./dto/add-category.dto";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class UserCheckRepository extends Repository<UserEntity> {
  constructor(dataSource: DataSource) {
    super(UserEntity, dataSource.createEntityManager());
  }

  async checkByUserUuid(dto: AddCategoryDto): Promise<UserEntity> {
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
