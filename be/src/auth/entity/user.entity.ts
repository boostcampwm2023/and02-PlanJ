import { BaseEntity, Column, DeleteDateColumn, Entity, PrimaryColumn } from "typeorm";
import { Exclude } from "class-transformer";

@Entity("User")
export class UserEntity extends BaseEntity {
  @PrimaryColumn()
  userId: string;

  @Column({ length: 60 })
  email: string;

  @Column({ length: 60 })
  password: string;

  @Column({ length: 12 })
  nickname: string;

  @Column({ default: 0 })
  point: number;

  @Exclude()
  @DeleteDateColumn({ type: "timestamp" })
  deletedAt?: Date | null;

  @Column({ nullable: true })
  acceptNotification: boolean;
}
