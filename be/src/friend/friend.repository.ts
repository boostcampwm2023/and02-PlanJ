import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { FriendEntity } from "./entity/friend.entity";
import { DataSource, Repository } from "typeorm";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class FriendRepository extends Repository<FriendEntity> {
  constructor(dataSource: DataSource) {
    super(FriendEntity, dataSource.createEntityManager());
  }

  async add(from: UserEntity, to: UserEntity): Promise<void> {
    const { userId: fromUserId } = from;
    const { userId: toUserId } = to;
    const record = this.create({ from: from, to: toUserId });
    const reverseRecord = this.create({ from: to, to: fromUserId });

    await this.save(record);
    await this.save(reverseRecord);
  }
}
