import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { ScheduleLocationEntity } from "./entity/schedule-location.entity";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";

@Injectable()
export class ScheduleLocationRepository extends Repository<ScheduleLocationEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleLocationEntity, dataSource.createEntityManager());
  }
  async addNullLocation(scheduleMeta: ScheduleMetadataEntity) {
    const placeName = null;
    const placeAddress = null;
    const latitude = null;
    const longitude = null;

    const record = this.create({ placeName, placeAddress, latitude, longitude, scheduleMeta });

    try {
      await this.save(record);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetadataEntity): Promise<void> {
    const { placeName, placeAddress, latitude, longitude } = dto;

    const record = await this.createQueryBuilder("location")
      .leftJoinAndSelect("location.scheduleMeta", "scheduleMeta")
      .where("location.metadata_id = :id", { id: scheduleMeta.metadataId })
      .getOne();

    console.log(placeName, placeAddress, latitude, longitude);

    record.placeName = placeName;
    record.placeAddress = placeAddress;
    record.latitude = latitude !== null ? parseFloat(latitude) : null;
    record.longitude = longitude !== null ? parseFloat(longitude) : null;

    try {
      await this.save(record);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }
}
