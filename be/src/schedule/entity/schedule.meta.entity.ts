import {
  BaseEntity,
  Column,
  Entity,
  JoinColumn,
  ManyToOne,
  OneToMany,
  PrimaryColumn,
  PrimaryGeneratedColumn,
} from "typeorm";
import { ScheduleEntity } from "./schedule.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryEntity } from "src/category/entity/category.entity";

@Entity("ScheduleMeta")
export class ScheduleMetaEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "metadata_id" })
  metadataId: number;

  @Column({ length: 20 })
  title: string;

  @Column({ length: 256 })
  description: string;

  @Column({ nullable: true, name: "start_time", type: "time", default: null })
  startTime: Date;

  @Column({ name: "end_time", type: "time" })
  endTime: Date;

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
