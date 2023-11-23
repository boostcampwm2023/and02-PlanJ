import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { ScheduleLocationEntity } from "./entity/schedule-location.entity";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";

@Injectable()
export class ScheduleLocationRepository extends Repository<ScheduleLocationEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleLocationEntity, dataSource.createEntityManager());
  }

  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetaEntity): Promise<void> {
    const { placeName, placeAddress, latitude, longtitude } = dto;

    let record = await this.createQueryBuilder("location")
      .leftJoinAndSelect("location.scheduleMeta", "scheduleMeta")
      .where("location.metadata_id = :id", { id: scheduleMeta.metadataId })
      .getOne();

    console.log(record);

    if (record) {
      record.placeName = placeName;
      record.placeAddress = placeAddress;
      record.latitude = parseFloat(latitude);
      record.longtitude = parseFloat(longtitude);
      scheduleMeta = scheduleMeta;
    } else {
      record = this.create({
        placeName,
        placeAddress,
        latitude: parseFloat(latitude),
        longtitude: parseFloat(longtitude),
        scheduleMeta,
      });
    }

    try {
      await this.save(record);
    } catch (error) {
      console.log(error); //logger
      throw new InternalServerErrorException();
    }
  }
}
