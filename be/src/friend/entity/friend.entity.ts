import { BaseEntity, Column, DeleteDateColumn, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { UserEntity } from "src/user/entity/user.entity";

@Entity("friend")
export class FriendEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "friend_id" })
  friendId: number;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  @Column({ name: "to" })
  to: number;

  /*
   * relation
   */
  @OneToOne(() => UserEntity, (user) => user.userId)
  @JoinColumn({ name: "from" })
  from: UserEntity;
}
