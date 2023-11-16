import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleRepository } from "./schedule.repository";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleRepository)
    private scheduleRepository: ScheduleRepository,
  ) {}

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    return await this.scheduleRepository.addSchedule(dto);
  }

  getDaily() {}

  getWeekly() {}
}
