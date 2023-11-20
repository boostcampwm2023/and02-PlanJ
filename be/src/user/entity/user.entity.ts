import { BaseEntity, Column, Entity, OneToMany, PrimaryGeneratedColumn, Unique } from "typeorm";
import { CategoryEntity } from "src/category/entity/category.entity";
import { ScheduleMetaEntity } from "src/schedule/entity/schedule.meta.entity";

@Entity("User")
// @Unique(["email"])
export class UserEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "user_id" })
  userId: number;

  @Column({ length: 26, name: "user_uuid" })
  userUuid: string;

  @Column({ length: 60 })
  email: string;

  @Column({ length: 60 })
  password: string;

  @Column({ length: 12 })
  nickname: string;

  @Column({ type: "timestamp", name: "created_at" })
  createdAt: Date;

  @Column({ default: null, type: "timestamp", name: "updated_at" })
  updatedAt?: Date | null;

  @Column({ default: false })
  deleted: boolean;

  @Column({ default: 0 })
  point: number;

  @Column({ nullable: true, name: "accept_notification" })
  acceptNotification: boolean;

  /*
   * relation
   * */
  @OneToMany(() => CategoryEntity, (category) => category.user, {
    cascade: true,
  })
  category: CategoryEntity[];

  // participant 추가 시 삭제될 관계
  @OneToMany(() => ScheduleMetaEntity, (scheduleMeta) => scheduleMeta.user, {
    cascade: true,
  })
  scheduleMeta: ScheduleMetaEntity[];
}
