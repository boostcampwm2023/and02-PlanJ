import { UserEntity } from "src/user/entity/user.entity";
import { BaseEntity, Column, DeleteDateColumn, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";

@Entity("friends")
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
  @JoinColumn()
  from: UserEntity;

  @OneToOne(() => UserEntity, (user) => user.userId)
  @JoinColumn()
  to: UserEntity;
}
