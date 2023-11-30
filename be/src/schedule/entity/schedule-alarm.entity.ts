import { BaseEntity, Column, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("schedule_alarm")
export class ScheduleAlarmEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id", type: "int" })
  alarmId: number;

  @Column({ name: "metadata_id", type: "int" })
  metadataId: number;

  @Column({ name: "type", type: "tinyint" })
  alarmType: number;

  @Column({ name: "time", type: "time" })
  alarmTime: string;

  @OneToOne(() => ScheduleMetadataEntity, (scheduleMetadata) => scheduleMetadata.metadataId)
  @JoinColumn({ name: "metadata_id" })
  scheduleMetadata: ScheduleMetadataEntity;
}
