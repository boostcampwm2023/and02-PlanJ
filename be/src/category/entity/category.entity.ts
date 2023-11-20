import { UserEntity } from "src/user/entity/user.entity";
import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryColumn, PrimaryGeneratedColumn } from "typeorm";

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
}
