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

  @Column({ name: "from" })
  fromId: number;

  @Column({ name: "to" })
  toId: number;

  /*
   * relation
   */
  @ManyToOne(() => UserEntity, (user) => user.friend, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "from" })
  from: UserEntity;
}
