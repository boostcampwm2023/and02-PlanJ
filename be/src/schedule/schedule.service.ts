import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleRepository } from "./schedule.repository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";
import { ScheduleEntity } from "./entity/schedule.entity";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleRepository)
    private scheduleRepository: ScheduleRepository,
  ) {}

  async addSchedule(dto: AddScheduleDto, scheduleMetadata: ScheduleMetadataEntity): Promise<string> {
    return await this.scheduleRepository.addSchedule(dto, scheduleMetadata);
  }

  async getMetadataIdByScheduleUuid(scheduleUuid: string): Promise<number> {
    return await this.scheduleRepository.getMetadataIdByScheduleUuid(scheduleUuid);
  }

  async updateSchedule(dto: UpdateScheduleDto): Promise<string> {
    // TODO: repetition 기준으로 추가
    return await this.scheduleRepository.updateSchedule(dto);
  }

  async deleteSchedule(dto: DeleteScheduleDto) {
    return await this.scheduleRepository.deleteSchedule(dto);
  }

  async getScheduleEntityByScheduleUuid(scheduleUuid: string): Promise<ScheduleEntity> {
    return await this.scheduleRepository.findOne({ where: { scheduleUuid }, relations: ["parent"] });
  }
}
