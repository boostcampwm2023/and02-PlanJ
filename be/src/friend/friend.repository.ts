import { Injectable } from "@nestjs/common";
import { FriendEntity } from "./entity/friend.entity";
import { DataSource, Repository } from "typeorm";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class FriendRepository extends Repository<FriendEntity> {
  constructor(dataSource: DataSource) {
    super(FriendEntity, dataSource.createEntityManager());
  }

  async add(from: UserEntity, to: UserEntity): Promise<void> {
    const { userId: fromId } = from;
    const { userId: toId } = to;
    const record = this.create({ fromId: fromId, toId: toId });
    const reverseRecord = this.create({ fromId: toId, toId: fromId });

    await this.save(record);
    await this.save(reverseRecord);
  }

  async getAllFriends(userEntity: UserEntity): Promise<FriendEntity[]> {
    return await this.find({ where: { fromId: userEntity.userId } });
  }
}
