import { ScheduleRepository } from "./schedule.repository";
import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleMetaRepository } from "./schedule.meta.repository";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleMetaRepository)
    private scheduleMetaRepository: ScheduleMetaRepository,
    private scheduleRepository: ScheduleRepository,
  ) {}

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    await this.scheduleMetaRepository.addScheduleMeta(dto);
    return await this.scheduleRepository.addSchedule(dto);
  }

  getDaily() {}

  getWeekly() {}
}
