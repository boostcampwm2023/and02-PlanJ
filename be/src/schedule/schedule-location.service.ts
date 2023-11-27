import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleLocationRepository } from "./schedule-location.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleLocation } from "src/utils/location.interface";

@Injectable()
export class ScheduleLocationService {
  constructor(
    @InjectRepository(ScheduleLocationRepository)
    private scheduleLocationRepository: ScheduleLocationRepository,
  ) {}

  async addLocation(
    startLocation: ScheduleLocation,
    endLocation: ScheduleLocation,
    scheduleMeta: ScheduleMetadataEntity,
  ) {
    await this.scheduleLocationRepository.addLocation(startLocation, endLocation, scheduleMeta);
  }

  async updateLocation(
    startLocation: ScheduleLocation,
    endLocation: ScheduleLocation,
    scheduleMeta: ScheduleMetadataEntity,
  ): Promise<void> {
    await this.scheduleLocationRepository.updateLocation(startLocation, endLocation, scheduleMeta);
  }
}
