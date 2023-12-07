import { BaseEntity, Column, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("repetition")
export class RepetitionEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id" })
  id: number;

  @Column({ name: "metadata_id", type: "int" })
  metadataId: number;

  @Column({ name: "cycle_type", length: 6 })
  cycleType: string;

  @Column({ name: "cycle_count", type: "int" })
  cycleCount: number;

  /*
   * relation
   */
  @OneToOne(() => ScheduleMetadataEntity, (scheduleMetadata) => scheduleMetadata.metadataId)
  @JoinColumn({ name: "metadata_id" })
  scheduleMeta: ScheduleMetadataEntity;
}
