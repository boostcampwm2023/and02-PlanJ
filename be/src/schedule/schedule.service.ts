import { ScheduleRepository } from "./schedule.repository";
import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleMetaRepository } from "./schedule.meta.repository";
import { UserCheckRepository } from "./user.check.repository";
import { CategoryCheckRepository } from "./category.check.repository";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleMetaRepository)
    private scheduleMetaRepository: ScheduleMetaRepository,
    private scheduleRepository: ScheduleRepository,
    private userCheckRepository: UserCheckRepository,
    private categoryCheckRepository: CategoryCheckRepository,
  ) {}

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    const user = await this.userCheckRepository.checkByUserUuid(dto);
    const category = await this.categoryCheckRepository.checkByCategoryUuid(dto);
    const schdeuleMetadata = await this.scheduleMetaRepository.addScheduleMeta(dto, user, category);
    return await this.scheduleRepository.addSchedule(dto, schdeuleMetadata);
  }

  getDaily() {}

  getWeekly() {}
}
