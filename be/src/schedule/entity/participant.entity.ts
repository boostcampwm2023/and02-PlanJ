import { BaseEntity, Column, DeleteDateColumn, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("participant")
export class ParticipantEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id" })
  id: number;

  @Column({ name: "participant_id", type: "int" })
  participantId: number;

  @Column({ name: "author_id", type: "int" })
  authorId: number;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  /*
   * relation
   */
  @ManyToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.participant)
  @JoinColumn({ name: "participant_id" })
  participant: ScheduleMetadataEntity;

  @ManyToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.author, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "author_id" })
  author: ScheduleMetadataEntity;
}
