import { DataSource, Repository } from "typeorm";
import { UserEntity } from "./entity/user.entity";
import { Injectable, UnauthorizedException } from "@nestjs/common";
import { HttpResponse } from "../utils/http.response";
import { UserModifyDto } from "./dto/user-modify.dto";

@Injectable()
export class UserRepository extends Repository<UserEntity> {
  constructor(dataSource: DataSource) {
    super(UserEntity, dataSource.createEntityManager());
  }

  async checkUserExists(email: string) {
    const user = await this.findOne({
      where: { email: email },
    });
    return user !== null;
  }

  async deleteUser(userUuid: string): Promise<void> {
    await this.softDelete({ userUuid: userUuid });
  }

  private async getUserEntityByUuid(userUuid: string) {
    const user = await this.findOne({
      where: { userUuid: userUuid },
    });

    if (user === null) {
      throw new UnauthorizedException("존재하지 않는 user");
    }
    return user;
  }
}
