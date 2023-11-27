import { BaseEntity, Column, DeleteDateColumn, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("schedule")
export class ScheduleEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "schedule_id" })
  scheduleId: number;

  @Column({ length: 26, name: "schedule_uuid" })
  scheduleUuid: string;

  @Column({ nullable: true, default: null, name: "start_at" })
  startAt: string;

  @Column({ name: "end_at" })
  endAt: string;

  @Column({ default: false })
  finished: boolean;

  @Column({ default: false })
  failed: boolean;

  @Column({ length: 256, name: "remind_memo", default: null, nullable: true })
  remindMemo: string;

  @Column({ default: true })
  last: boolean;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  /*
   * relation
   */
  @ManyToOne(() => ScheduleMetadataEntity, (parent) => parent.metadataId, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "metadata_id" })
  parent?: ScheduleMetadataEntity;
}
