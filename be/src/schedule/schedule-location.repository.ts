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

  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetadataEntity): Promise<void> {
    const { placeName, placeAddress, latitude, longitude } = dto;

    let record = await this.createQueryBuilder("location")
      .leftJoinAndSelect("location.scheduleMeta", "scheduleMeta")
      .where("location.metadata_id = :id", { id: scheduleMeta.metadataId })
      .getOne();

    if (record) {
      record.placeName = placeName;
      record.placeAddress = placeAddress;
      record.latitude = parseFloat(latitude);
      record.longitude = parseFloat(longitude);
    } else {
      record = this.create({
        placeName,
        placeAddress,
        latitude: parseFloat(latitude),
        longitude: parseFloat(longitude),
        scheduleMeta,
      });
    }

    try {
      await this.save(record);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }
}
