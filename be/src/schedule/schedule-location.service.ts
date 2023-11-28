import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleLocationRepository } from "./schedule-location.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleLocationDto } from "src/schedule/dto/schedule-location.dto";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";

@Injectable()
export class ScheduleLocationService {
  constructor(
    @InjectRepository(ScheduleLocationRepository)
    private scheduleLocationRepository: ScheduleLocationRepository,
  ) {}
  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetadataEntity): Promise<void> {
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
    } else {
      record.startPlaceName = startPlaceName;
      record.startPlaceAddress = startPlaceAddress;
      record.startLatitude = startLatitude;
      record.startLongitude = startLongitude;

      record.endPlaceName = endPlaceName;
      record.endPlaceAddress = endPlaceAddress;
      record.endLatitude = endLatitude;
      record.endLongitude = endLongitude;
    }

    try {
      await this.scheduleLocationRepository.save(record);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }
}
