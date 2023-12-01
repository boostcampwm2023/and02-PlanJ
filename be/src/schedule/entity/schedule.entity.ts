import { BaseEntity, Column, DeleteDateColumn, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("schedule")
export class ScheduleEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id" })
  scheduleId: number;

  @Column({ length: 26, name: "uuid" })
  scheduleUuid: string;

  @Column({ nullable: true, default: null, name: "start_at" })
  startAt: string;

  @Column({ name: "end_at" })
  endAt: string;

  @Column({ default: false })
  finished: boolean;

  @Column({ default: false })
  failed: boolean;

  @Column({ length: 256, name: "retrospective_memo", default: null, nullable: true })
  retrospectiveMemo: string;

  @Column({ default: true })
  last: boolean;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  @Column({ name: "metadata_id" })
  metadataId: number;

  /*
   * relation
   */
  @ManyToOne(() => ScheduleMetadataEntity, (parent) => parent.metadataId, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "metadata_id" })
  parent?: ScheduleMetadataEntity;
}
