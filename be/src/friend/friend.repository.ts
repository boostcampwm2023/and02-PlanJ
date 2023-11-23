import { Injectable } from "@nestjs/common";
import { FriendsEntity } from "./entity/friends.entity";
import { DataSource, Repository } from "typeorm";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class FriendRepository extends Repository<FriendsEntity> {
  constructor(dataSource: DataSource) {
    super(FriendsEntity, dataSource.createEntityManager());
  }

  async add(from: UserEntity, to: UserEntity) {
    const friend = this.create({ from, to });

    console.log(friend);

    await this.save(friend.from);
    await this.save(friend.to);
  }
}
