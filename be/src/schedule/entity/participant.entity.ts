import { BaseEntity, Column, Entity, ManyToOne, PrimaryColumn } from "typeorm";
import { UserEntity } from "../../auth/entity/user.entity";
import { ScheduleMetaEntity } from "./schedule.meta.entity";
import { JoinColumn } from "typeorm";

@Entity("Participant")
export class ParticipantEntity extends BaseEntity {
  @PrimaryColumn({ name: "user_id" })
  userId: string;

  @PrimaryColumn({ name: "meta_id" })
  metaId: string;

  @Column()
  author: boolean;

  /*
   * relation
   */
  @ManyToOne(() => UserEntity, (user: UserEntity) => user.userId, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "user_id" })
  user: UserEntity;

  @ManyToOne(() => ScheduleMetaEntity, (meta: ScheduleMetaEntity) => meta.metaId, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "meta_id" })
  meta: ScheduleMetaEntity;
}
