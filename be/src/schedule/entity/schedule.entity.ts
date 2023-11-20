import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetaEntity } from "./schedule.meta.entity";

@Entity("Schedule")
export class ScheduleEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "schedule_id" })
  scheduleId: number;

  @Column({ length: 26, name: "schedule_uuid" })
  scheduleUuid: string;

  @Column()
  startAt: Date;

  @Column()
  endAt: Date;

  @Column({ default: false })
  finished: boolean;

  @Column({ default: false })
  failed: boolean;

  @Column({ length: 256, name: "remind_memo", default: null, nullable: true })
  remindMemo: string;

  @Column({ default: true })
  last: boolean;

  /*
   * relation
   */
  @ManyToOne(() => ScheduleMetaEntity, (parent) => parent.metadataId, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "metadata_id" })
  parent?: ScheduleMetaEntity;
}
