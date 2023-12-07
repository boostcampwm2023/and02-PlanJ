import {
  BaseEntity,
  Column,
  DeleteDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
} from "typeorm";
import { ScheduleEntity } from "./schedule.entity";
import { CategoryEntity } from "src/category/entity/category.entity";
import { ParticipantEntity } from "./participant.entity";
import { UserEntity } from "src/user/entity/user.entity";

@Entity("schedule_metadata")
export class ScheduleMetadataEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id" })
  metadataId: number;

  @Column({ length: 20 })
  title: string;

  @Column({ length: 256, nullable: true, default: null })
  description: string;

  @Column({ nullable: true, name: "start_time", type: "time", default: null })
  startTime: string;

  @Column({ name: "end_time", type: "time" })
  endTime: string;

  @Column({ name: "user_id", type: "int" })
  userId: number;

  @Column({ name: "category_id", type: "int", nullable: true })
  categoryId: number;

  @Column({ type: "boolean", default: false })
  repeated: boolean;

  @Column({ type: "boolean", default: false })
  shared: boolean;

  @Column({ type: "boolean", default: false, name: "has_location" })
  hasLocation: boolean;

  @Column({ type: "boolean", default: false, name: "has_alarm" })
  hasAlarm: boolean;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  /*
   * relation
   */

  @ManyToOne(() => UserEntity, (user) => user.scheduleMeta, {
    onDelete: "CASCADE",
    onUpdate: "CASCADE",
  })
  @JoinColumn({ name: "user_id" })
  user: UserEntity;

  @ManyToOne(() => CategoryEntity, (category) => category.scheduleMeta, {
    onDelete: "CASCADE",
    onUpdate: "CASCADE",
  })
  @JoinColumn({ name: "category_id" })
  category: CategoryEntity;

  @OneToMany(() => ScheduleEntity, (schedule) => schedule.parent, {
    cascade: true,
  })
  children: ScheduleEntity[];

  @OneToMany(() => ParticipantEntity, (participant) => participant.author, {
    cascade: true,
  })
  author: ParticipantEntity[];

  @OneToMany(() => ParticipantEntity, (participant) => participant.participant, {
    cascade: true,
  })
  participant: ParticipantEntity[];
}
