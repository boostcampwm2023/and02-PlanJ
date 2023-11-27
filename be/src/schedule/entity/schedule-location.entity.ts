import { BaseEntity, Column, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("schedule_location")
export class ScheduleLocationEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "location_id" })
  locationId: number;

  @Column({ name: "place_name", nullable: true, default: null })
  placeName: string;

  @Column({ name: "place_address", nullable: true, default: null })
  placeAddress: string;

  @Column({ name: "latitude", type: "decimal", precision: 9, scale: 6, nullable: true, default: null })
  latitude: number;

  @Column({ name: "longitude", type: "decimal", precision: 9, scale: 6, nullable: true, default: null })
  longitude: number;

  /*
   * relation
   */
  @OneToOne(() => ScheduleMetadataEntity, (scheduleMetadata) => scheduleMetadata.metadataId)
  @JoinColumn({ name: "metadata_id" })
  scheduleMeta: ScheduleMetadataEntity;
}
