import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("participant")
export class ParticipantEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id" })
  id: number;

  @Column({ name: "participant_id", type: "int" })
  participantId: number;

  @Column({ name: "author_id", type: "int" })
  authorId: number;

  /*
   * relation
   */
  @ManyToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.metadataId)
  @JoinColumn({ name: "participant_id" })
  participant: ScheduleMetadataEntity;

  @ManyToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.author, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "author_id" })
  author: ScheduleMetadataEntity;
}
