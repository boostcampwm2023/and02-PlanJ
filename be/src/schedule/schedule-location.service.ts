import { Injectable, InternalServerErrorException, Logger } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { Repository } from "typeorm";
import { ScheduleLocationEntity } from "./entity/schedule-location.entity";

@Injectable()
export class ScheduleLocationService {
  private readonly logger = new Logger(ScheduleLocationService.name);
  constructor(
    @InjectRepository(ScheduleLocationEntity)
    private scheduleLocationRepository: Repository<ScheduleLocationEntity>,
  ) {}
  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetadataEntity, author: boolean): Promise<void> {
    let record = await this.scheduleLocationRepository.findOne({ where: { metadataId: scheduleMeta.metadataId } });

    // end location 이 null 일 때
    if (!dto.endLocation) {
      // record 가 존재할 때
      if (!!record) {
        await this.scheduleLocationRepository.remove(record);
      }
      return;
    }

    const {
      placeName: startPlaceName,
      placeAddress: startPlaceAddress,
      latitude: startLatitude,
      longitude: startLongitude,
    } = !!dto.startLocation
      ? dto.startLocation
      : { placeName: null, placeAddress: null, latitude: null, longitude: null };

    const {
      placeName: endPlaceName,
      placeAddress: endPlaceAddress,
      latitude: endLatitude,
      longitude: endLongitude,
    } = dto.endLocation;

    if (!record) {
      if (author) {
        record = this.scheduleLocationRepository.create({
          startPlaceName,
          startPlaceAddress,
          startLatitude,
          startLongitude,
          endPlaceName,
          endPlaceAddress,
          endLatitude,
          endLongitude,
          metadataId: scheduleMeta.metadataId,
        });
      }
    } else {
      record.startPlaceName = startPlaceName;
      record.startPlaceAddress = startPlaceAddress;
      record.startLatitude = startLatitude;
      record.startLongitude = startLongitude;

      if (author) {
        record.endPlaceName = endPlaceName;
        record.endPlaceAddress = endPlaceAddress;
        record.endLatitude = endLatitude;
        record.endLongitude = endLongitude;
      }
    }

    try {
      await this.scheduleLocationRepository.save(record);
    } catch (error) {
      this.logger.error(error);
      throw new InternalServerErrorException();
    }
  }

  async getLocationByScheduleMetadataId(scheduleMetaId: number): Promise<ScheduleLocationEntity> {
    return await this.scheduleLocationRepository.findOne({ where: { metadataId: scheduleMetaId } });
  }
}
