import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleLocationRepository } from "./schedule-location.repository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { AddScheduleDto } from "./dto/add-schedule.dto";

@Injectable()
export class ScheduleLocationService {
  constructor(
    @InjectRepository(ScheduleLocationRepository)
    private scheduleLocationRepository: ScheduleLocationRepository,
  ) {}

  async addLocation(dto: AddScheduleDto, scheduleMeta: ScheduleMetadataEntity) {
    await this.scheduleLocationRepository.addLocation(dto, scheduleMeta);
  }

  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetadataEntity): Promise<void> {
    await this.scheduleLocationRepository.updateLocation(dto, scheduleMeta);
  }
}
