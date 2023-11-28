import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("participant")
export class ParticipantEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "participant_id" })
  participantId: number;

  /*
   * relation
   */
  @OneToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.metadataId)
  @JoinColumn({ name: "metadata_id" })
  scheduleMeta: ScheduleMetadataEntity;

  @ManyToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.author, {
    onDelete: "CASCADE",
  })
  @JoinColumn()
  author: ScheduleMetadataEntity;
}
