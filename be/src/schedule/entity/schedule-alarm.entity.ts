import { BaseEntity, Column, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("schedule_alarm")
export class ScheduleAlarmEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id", type: "int" })
  alarmId: number;

  @Column({ name: "metadata_id", type: "int" })
  metadataId: number;

  @Column({ name: "alarm_type", length: 9 })
  alarmType: string;

  @Column({ name: "alarm_time", type: "int" })
  alarmTime: number;

  @Column({ name: "estimated_time", type: "int" })
  estimatedTime: number;

  @OneToOne(() => ScheduleMetadataEntity, (scheduleMetadata) => scheduleMetadata.metadataId)
  @JoinColumn({ name: "metadata_id" })
  scheduleMetadata: ScheduleMetadataEntity;
}
