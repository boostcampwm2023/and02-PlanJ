import { BaseEntity, Column, Decimal128, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetaEntity } from "./schedule-meta.entity";

@Entity("ScheduleLocation")
export class ScheduleLocationEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "location_id" })
  locationId: number;

  @Column({ name: "place_name" })
  placeName: string;

  @Column({ name: "place_address" })
  placeAddress: string;

  @Column({ name: "latitude", type: "decimal", precision: 9, scale: 6 })
  latitude: number;

  @Column({ name: "longtitude", type: "decimal", precision: 9, scale: 6 })
  longtitude: number;

  /*
   * relation
   */
  @OneToOne(() => ScheduleMetaEntity, (scheduleMetadata) => scheduleMetadata.metadataId)
  @JoinColumn({ name: "metadata_id" })
  scheduleMeta: ScheduleMetaEntity;
}
