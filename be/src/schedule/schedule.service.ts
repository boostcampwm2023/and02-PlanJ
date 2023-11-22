import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule-meta.entity";
import { ScheduleRepository } from "./schedule.repository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";

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

  async deleteSchedule(dto: DeleteScheduleDto) {
    return await this.scheduleRepository.deleteSchedule(dto);
  }
}
