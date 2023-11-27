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
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";

@Entity("schedule_metadata")
export class ScheduleMetadataEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "metadata_id" })
  metadataId: number;

  @Column({ length: 20 })
  title: string;

  @Column({ length: 256, nullable: true, default: null })
  description: string;

  @Column({ nullable: true, name: "start_time", type: "time", default: null })
  startTime: string;

  @Column({ name: "end_time", type: "time" })
  endTime: string;

  @Column({ name: "category_id", type: "int", nullable: true })
  categoryId: number;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  @Column({ name: "user_id", type: "int" })
  userId: number;

  /*
   * relation
   */
  @OneToMany(() => ScheduleEntity, (schedule) => schedule.parent, {
    cascade: true,
  })
  children: ScheduleEntity[];

  @ManyToOne(() => CategoryEntity, (category) => category.scheduleMeta, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "category_id" })
  category: CategoryEntity;

  // participant 추가 시 삭제될 관계
  @ManyToOne(() => UserEntity, (user) => user.scheduleMeta, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "user_id" })
  user: UserEntity;
}
