import { ScheduleMetadataEntity } from "src/schedule/entity/schedule-metadata.entity";
import { UserEntity } from "src/user/entity/user.entity";
import {
  BaseEntity,
  Column,
  CreateDateColumn,
  DeleteDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
} from "typeorm";

@Entity("category")
export class CategoryEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id", type: "int" })
  categoryId: number;

  @Column({ length: 26, name: "uuid" })
  categoryUuid: string;

  @Column({ length: 128, name: "name" })
  categoryName: string;

  @Column({ type: "int", name: "user_id" })
  userId: number;

  @CreateDateColumn({ type: "timestamp", name: "created_at" })
  createdAt: Date;

  @DeleteDateColumn({ default: null, name: "deleted_at" })
  deletedAt: Date | null;

  /*
   * relation
   * */
  @ManyToOne(() => UserEntity, (user) => user.category, {
    onDelete: "CASCADE",
    onUpdate: "CASCADE",
  })
  @JoinColumn({ name: "user_id" })
  user: UserEntity;

  @OneToMany(() => ScheduleMetadataEntity, (metadata) => metadata.category, {
    cascade: true,
  })
  scheduleMeta: ScheduleMetadataEntity[];
}
