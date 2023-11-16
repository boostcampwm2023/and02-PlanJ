import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { InjectRepository } from "@nestjs/typeorm";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleRepository)
    private scheduleRepository: ScheduleRepository,
  ) {}

  addSchedule(dto: AddScheduleDto) {
    return this.scheduleRepository.addSchedule(dto);
  }

  getDaily() {}

  getWeekly() {}
}
