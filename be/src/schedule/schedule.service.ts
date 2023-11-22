import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { ScheduleRepository } from "./schedule.repository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleRepository)
    private scheduleRepository: ScheduleRepository,
  ) {}

  async addSchedule(dto: AddScheduleDto, scheduleMetadata: ScheduleMetaEntity): Promise<string> {
    return await this.scheduleRepository.addSchedule(dto, scheduleMetadata);
  }

  async getMetadataIdByScheduleUuid(scheduleUuid: string): Promise<number> {
    return await this.scheduleRepository.getMetadataIdByScheduleUuid(scheduleUuid);
  }

  async updateSchedule(dto: UpdateScheduleDto): Promise<string> {
    return await this.scheduleRepository.updateSchedule(dto);
  }
}
