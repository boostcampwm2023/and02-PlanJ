import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { UserEntity } from "src/user/entity/user.entity";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("participant")
export class ParticipantEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "participant_id" })
  participantId: number;

  @Column({ default: false })
  author: boolean;

  /*
   * relation
   */

  // participant 추가 시 삭제될 관계
  @ManyToOne(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.participant, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "metadata_id" })
  scheduleMeta: ScheduleMetadataEntity;
}
