import {
  BaseEntity,
  Column,
  CreateDateColumn,
  DeleteDateColumn,
  Entity,
  OneToMany,
  PrimaryGeneratedColumn,
} from "typeorm";
import { CategoryEntity } from "src/category/entity/category.entity";
import { ScheduleMetadataEntity } from "src/schedule/entity/schedule-metadata.entity";

@Entity("user")
export class UserEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id" })
  userId: number;

  @Column({ length: 26, name: "uuid" })
  userUuid: string;

  @Column({ length: 60 })
  email: string;

  @Column({ length: 60, nullable: true })
  password: string;

  @Column({ length: 256, nullable: true, name: "naver_id" })
  naverId: string;

  @Column({ length: 12 })
  nickname: string;

  @CreateDateColumn({ type: "timestamp", name: "created_at" })
  createdAt: Date;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  @Column({ default: 0 })
  point: number;

  @Column({ nullable: true, name: "accept_notification" })
  acceptNotification: boolean;

  @Column({ nullable: true, name: "device_token", default: null })
  deviceToken: string;

  @Column({ nullable: true, default: null, length: 256, name: "profile_url" })
  profileUrl: string;

  /*
   * relation
   * */
  @OneToMany(() => CategoryEntity, (category) => category.user, {
    cascade: true,
  })
  category: CategoryEntity[];

  @OneToMany(() => ScheduleMetadataEntity, (scheduleMeta) => scheduleMeta.user, {
    cascade: true,
  })
  scheduleMeta: ScheduleMetadataEntity[];
}
