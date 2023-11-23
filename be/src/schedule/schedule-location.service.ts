import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleLocationRepository } from "./schedule-location.respository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";

@Injectable()
export class ScheduleLocationService {
  constructor(
    @InjectRepository(ScheduleLocationRepository)
    private scheduleLocationRepository: ScheduleLocationRepository,
  ) {}

  async updateLocation(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetaEntity): Promise<void> {
    await this.scheduleLocationRepository.updateLocation(dto, scheduleMeta);
  }
}
