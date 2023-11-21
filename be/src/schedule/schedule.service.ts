import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetaEntity } from "./entity/schedule.meta.entity";
import { ScheduleRepository } from "./schedule.repository";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleRepository)
    private scheduleRepository: ScheduleRepository,
  ) {}

  async addSchedule(dto: AddScheduleDto, scheduleMetadata: ScheduleMetaEntity): Promise<string> {
    return await this.scheduleRepository.addSchedule(dto, scheduleMetadata);
  }
}
