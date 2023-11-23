import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleLocationRepository } from "./schedule-location.respository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";

@Injectable()
export class ScheduleLocationService {
  constructor(
    @InjectRepository(ScheduleLocationRepository)
    private scheduleLocationRepository: ScheduleLocationRepository,
  ) {}

  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetadataEntity): Promise<void> {
    await this.scheduleLocationRepository.updateLocation(dto, scheduleMeta);
  }
}
