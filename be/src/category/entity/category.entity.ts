import { ScheduleMetaEntity } from "src/schedule/entity/schedule.meta.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";

@Entity("Category")
export class CategoryEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "category_id" })
  categoryId: string;

  @Column({ length: 26 })
  categoryUuid: string;

  @Column({ length: 128 })
  categoryName: string;

  @Column({ type: "timestamp", name: "created_at" })
  createdAt: Date;

  @Column({ default: false })
  deleted: boolean;

  /*
   * relation
   * */
  @ManyToOne(() => UserEntity, (user) => user.category, {
    onDelete: "CASCADE",
  })
  @JoinColumn({ name: "user_id" })
  user: UserEntity;

  @OneToMany(() => ScheduleMetaEntity, (scheduleMeta) => scheduleMeta.category, {
    cascade: true,
  })
  scheduleMeta: ScheduleMetaEntity[];
}
