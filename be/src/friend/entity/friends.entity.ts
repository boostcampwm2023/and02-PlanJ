import { UserEntity } from "src/user/entity/user.entity";
import { BaseEntity, Column, DeleteDateColumn, Entity, OneToOne, PrimaryGeneratedColumn } from "typeorm";

@Entity("Friends")
export class FriendsEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "friend_id" })
  friendId: number;

  @Column({ type: "timestamp", name: "created_at" })
  createdAt: Date;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  /*
   * relation
   */
  @OneToOne(() => UserEntity, (user) => user.userId)
  from: UserEntity;

  @OneToOne(() => UserEntity, (user) => user.userId)
  to: UserEntity;
}
