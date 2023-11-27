import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleLocationRepository } from "./schedule-location.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleLocationDto } from "src/schedule/dto/schedule-location.dto";

@Injectable()
export class ScheduleLocationService {
  constructor(
    @InjectRepository(ScheduleLocationRepository)
    private scheduleLocationRepository: ScheduleLocationRepository,
  ) {}

  async addLocation(
    startLocation: ScheduleLocationDto,
    endLocation: ScheduleLocationDto,
    scheduleMeta: ScheduleMetadataEntity,
  ) {
    await this.scheduleLocationRepository.addLocation(startLocation, endLocation, scheduleMeta);
  }

  async updateLocation(
    startLocation: ScheduleLocationDto,
    endLocation: ScheduleLocationDto,
    scheduleMeta: ScheduleMetadataEntity,
  ): Promise<void> {
    await this.scheduleLocationRepository.updateLocation(startLocation, endLocation, scheduleMeta);
  }
}
