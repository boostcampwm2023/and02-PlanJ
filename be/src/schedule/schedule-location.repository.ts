import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { ScheduleLocationEntity } from "./entity/schedule-location.entity";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleLocation } from "src/utils/location.interface";

@Injectable()
export class ScheduleLocationRepository extends Repository<ScheduleLocationEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleLocationEntity, dataSource.createEntityManager());
  }
  async addLocation(
    startLocation: ScheduleLocation,
    endLocation: ScheduleLocation,
    scheduleMeta: ScheduleMetadataEntity,
  ) {
    const {
      placeName: startPlaceName,
      placeAddress: startPlaceAddress,
      latitude: startLatitude,
      longitude: startLongitude,
    } = startLocation;

    const {
      placeName: endPlaceName,
      placeAddress: endPlaceAddress,
      latitude: endLatitude,
      longitude: endLongitude,
    } = endLocation;

    const record = this.create({
      startPlaceName,
      startPlaceAddress,
      startLatitude: startLatitude !== null ? parseFloat(startLatitude) : null,
      startLongitude: startLongitude !== null ? parseFloat(startLongitude) : null,
      endPlaceName,
      endPlaceAddress,
      endLatitude: endLatitude !== null ? parseFloat(endLatitude) : null,
      endLongitude: endLongitude !== null ? parseFloat(endLongitude) : null,
      scheduleMeta,
    });

    try {
      await this.save(record);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async updateLocation(
    startLocation: ScheduleLocation,
    endLocation: ScheduleLocation,
    scheduleMeta: ScheduleMetadataEntity,
  ): Promise<void> {
    const {
      placeName: startPlaceName,
      placeAddress: startPlaceAddress,
      latitude: startLatitude,
      longitude: startLongitude,
    } = startLocation;

    const {
      placeName: endPlaceName,
      placeAddress: endPlaceAddress,
      latitude: endLatitude,
      longitude: endLongitude,
    } = endLocation;

    const record = await this.createQueryBuilder("location")
      .leftJoinAndSelect("location.scheduleMeta", "scheduleMeta")
      .where("location.metadata_id = :id", { id: scheduleMeta.metadataId })
      .getOne();

    record.startPlaceName = startPlaceName;
    record.startPlaceAddress = startPlaceAddress;
    record.startLatitude = startLatitude !== null ? parseFloat(startLatitude) : null;
    record.startLongitude = startLongitude !== null ? parseFloat(startLongitude) : null;

    record.endPlaceName = endPlaceName;
    record.endPlaceAddress = endPlaceAddress;
    record.endLatitude = endLatitude !== null ? parseFloat(endLatitude) : null;
    record.endLongitude = endLongitude !== null ? parseFloat(endLongitude) : null;

    try {
      await this.save(record);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }
}
