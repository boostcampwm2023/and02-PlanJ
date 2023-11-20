import { BaseEntity, Column, DeleteDateColumn, Entity, OneToMany, PrimaryColumn } from "typeorm";
import { ParticipantEntity } from "../../schedule/entity/participant.entity";

@Entity("User")
export class UserEntity extends BaseEntity {
  @PrimaryColumn({ name: "user_id" })
  userId: string;

  @Column({ length: 60 })
  email: string;

  @Column({ length: 60 })
  password: string;

  @Column({ length: 12 })
  nickname: string;

  @Column({ type: "timestamp", name: "created_at" })
  createdAt: Date;

  @DeleteDateColumn({ type: "timestamp", name: "delete_at" })
  updatedAt?: Date | null;

  @Column({ default: false })
  deleted: boolean;

  @Column({ default: 0 })
  point: number;

  @Column({ nullable: true, name: "accept_notification" })
  acceptNotification: boolean;

  /*
   * relation
   * */
  @OneToMany(() => ParticipantEntity, (participant) => participant.user, {
    cascade: true,
  })
  participant: ParticipantEntity[];
}
