import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleRepository } from "./schedule.repository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";
import { ulid } from "ulid";
import { ScheduleEntity } from "./entity/schedule.entity";

@Injectable()
export class ScheduleService {
  constructor(
    @InjectRepository(ScheduleRepository)
    private scheduleRepository: ScheduleRepository,
  ) {}

  async addSchedule(dto: AddScheduleDto, scheduleMetadata: ScheduleMetadataEntity): Promise<string> {
    const { endAt } = dto;

    const scheduleUuid = ulid();

    const schedule = this.scheduleRepository.create({
      scheduleUuid,
      startAt: null,
      endAt,
      finished: false,
      failed: false,
      remindMemo: "",
      last: true,
      metadataId: scheduleMetadata.metadataId,
    });

    try {
      await this.scheduleRepository.save(schedule);
      return scheduleUuid;
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async getMetadataIdByScheduleUuid(scheduleUuid: string): Promise<number> {
    return await this.scheduleRepository.getMetadataIdByScheduleUuid(scheduleUuid);
  }

  async updateSchedule(dto: UpdateScheduleDto): Promise<string> {
    const { scheduleUuid, startAt, endAt } = dto;
    const record = await this.scheduleRepository.findOne({ where: { scheduleUuid } });
    record.startAt = startAt;
    record.endAt = endAt;

    try {
      await this.scheduleRepository.save(record);
      return scheduleUuid;
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
    // TODO: repetition 기준으로 추가
  }

  async deleteSchedule(dto: DeleteScheduleDto) {
    return await this.scheduleRepository.deleteSchedule(dto);
  }

  async getScheduleEntityByScheduleUuid(scheduleUuid: string): Promise<ScheduleEntity> {
    return await this.scheduleRepository.findOne({ where: { scheduleUuid }, relations: ["parent"] });
  }
}
