import {
  BaseEntity,
  Column,
  DeleteDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  OneToOne,
  PrimaryGeneratedColumn,
} from "typeorm";
import { UserEntity } from "src/user/entity/user.entity";

@Entity("friend")
export class FriendEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "friend_id" })
  friendId: number;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  @Column({ name: "from_id" })
  fromId: number;

  @Column({ name: "to_id" })
  toId: number;

  /*
   * relation
   */
  @OneToOne(() => UserEntity, (user) => user.userId)
  @JoinColumn({ name: "from_id"})
  fromUser: UserEntity;

  @OneToOne(() => UserEntity, (user) => user.userId)
  @JoinColumn({ name: "to_id"})
  toUser: UserEntity;
}
