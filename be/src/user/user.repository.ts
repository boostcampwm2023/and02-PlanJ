import { DataSource, Repository } from "typeorm";
import { UserEntity } from "./entity/user.entity";
import { Injectable } from "@nestjs/common";

// TODO: naming rule 적용
@Injectable()
export class UserRepository extends Repository<UserEntity> {
  constructor(dataSource: DataSource) {
    super(UserEntity, dataSource.createEntityManager());
  }

  async checkExistsByEmail(email: string) {
    const user = await this.findOne({
      where: { email: email },
    });
    return user !== null;
  }

  async deleteByUuid(userUuid: string): Promise<void> {
    await this.softDelete({ userUuid: userUuid });
  }

  async findByUuid(userUuid: string) {
    return await this.findOne({
      where: { userUuid: userUuid },
    });
  }

  async findById(userId: number) {
    return await this.findOne({
      where: { userId },
    });
  }

  async findByEmail(userEmail: string) {
    return await this.findOne({
      where: { email: userEmail },
    });
  }
}
