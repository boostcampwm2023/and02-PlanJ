import { BaseEntity, Column, Entity, JoinColumn, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { ScheduleMetadataEntity } from "./schedule-metadata.entity";

@Entity("schedule_location")
export class ScheduleLocationEntity extends BaseEntity {
  @PrimaryGeneratedColumn({ name: "id" })
  locationId: number;

  @Column({ name: "start_place_name", nullable: true, default: null })
  startPlaceName: string;

  @Column({ name: "start_place_address", nullable: true, default: null })
  startPlaceAddress: string;

  @Column({ name: "start_latitude", type: "decimal", precision: 9, scale: 6, nullable: true, default: null })
  startLatitude: number;

  @Column({ name: "start_longitude", type: "decimal", precision: 9, scale: 6, nullable: true, default: null })
  startLongitude: number;

  @Column({ name: "end_place_name", nullable: true, default: null })
  endPlaceName: string;

  @Column({ name: "end_place_address", nullable: true, default: null })
  endPlaceAddress: string;

  @Column({ name: "end_latitude", type: "decimal", precision: 9, scale: 6, nullable: true, default: null })
  endLatitude: number;

  @Column({ name: "end_longitude", type: "decimal", precision: 9, scale: 6, nullable: true, default: null })
  endLongitude: number;

  @Column({ name: "metadata_id", type: "int" })
  metadataId: number;

  /*
   * relation
   */
  @OneToOne(() => ScheduleMetadataEntity, (scheduleMetadata) => scheduleMetadata.metadataId)
  @JoinColumn({ name: "metadata_id" })
  scheduleMeta: ScheduleMetadataEntity;
}
