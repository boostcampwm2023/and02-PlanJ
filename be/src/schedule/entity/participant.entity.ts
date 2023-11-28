import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("participant")
export class ParticipantEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "participant_id" })
  participantId: number;

  @Column({ name: "participant_people_id", type: "int" })
  participantPeopleId: number;

  @Column({ name: "author_id", type: "int" })
  authorId: number;

  /*
   * relation
   */
  @OneToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.metadataId)
  @JoinColumn({ name: "participant_people_id" })
  participantPeople: ScheduleMetadataEntity;

  // participant 추가 시 삭제될 관계
  @ManyToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.author, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "author_id" })
  author: ScheduleMetadataEntity;
}
