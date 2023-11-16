import {BaseEntity, Column, DeleteDateColumn, Entity, OneToMany, PrimaryColumn} from "typeorm";
import { Exclude } from "class-transformer";
import {ParticipantEntity} from "../../schedule/entity/participant.entity";

@Entity("User")
export class UserEntity extends BaseEntity {
  @PrimaryColumn({name:"user_id"})
  userId: string;

  @Column({ length: 60 })
  email: string;

  @Column({ length: 60 })
  password: string;

  @Column({ length: 12 })
  nickname: string;

  @Column({ default: 0 })
  point: number;

  @Exclude()
  @DeleteDateColumn({ type: "timestamp", name: "delete_at" })
  deletedAt?: Date | null;

  @Column({ nullable: true, name:"accept_notification" })
  acceptNotification: boolean;

  /*
  * relation
  * */
  @OneToMany(() => ParticipantEntity, (participant) => participant.user, {
    cascade: true,
  })
  participant: ParticipantEntity[];
}
