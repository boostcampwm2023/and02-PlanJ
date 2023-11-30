import { BadRequestException, Injectable, InternalServerErrorException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleRepository } from "./schedule.repository";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";
import { ulid } from "ulid";
import { ScheduleEntity } from "./entity/schedule.entity";
import { RepetitionDto } from "./dto/repetition.dto";
import { add, addWeeks } from "date-fns";

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

  async updateSchedule(dto: UpdateScheduleDto, scheduleMetadata: ScheduleMetadataEntity): Promise<string> {
    const { scheduleUuid, startAt, endAt } = dto;
    const record = await this.scheduleRepository.findOne({ where: { scheduleUuid } });
    record.startAt = startAt;
    record.endAt = endAt;

    if (scheduleMetadata.repeated) {
      record.last = false;
    }

    try {
      await this.scheduleRepository.save(record);
      if (scheduleMetadata.repeated) {
        await this.addRepeatedSchedule(record, dto.repetition);
      }
      return scheduleUuid;
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  async deleteSchedule(dto: DeleteScheduleDto) {
    return await this.scheduleRepository.deleteSchedule(dto);
  }

  private async addRepeatedSchedule(record: ScheduleEntity, repetition: RepetitionDto) {
    const result: ScheduleEntity[] = [];
    let startDateTime = !!record.startAt ? new Date(record.startAt) : null;
    let endDateTime = new Date(record.endAt);
    startDateTime = !!startDateTime ? add(startDateTime, { hours: 9 }) : null;
    endDateTime = add(endDateTime, { hours: 9 });

    for (let i = 0; i < 30; i++) {
      const scheduleUuid = ulid();
      if (repetition.cycleType === "WEEKLY") {
        startDateTime = !!startDateTime ? addWeeks(startDateTime, repetition.cycleCount) : null;
        endDateTime = addWeeks(endDateTime, repetition.cycleCount);
      } else {
        startDateTime = !!startDateTime ? add(startDateTime, { days: repetition.cycleCount }) : null;
        endDateTime = add(endDateTime, { days: repetition.cycleCount });
      }

      result.push(
        this.scheduleRepository.create({
          scheduleUuid,
          startAt: !!startDateTime ? startDateTime.toISOString().slice(0, 19) : null,
          endAt: endDateTime.toISOString().slice(0, 19),
          finished: false,
          failed: false,
          remindMemo: "",
          last: false,
          metadataId: record.metadataId,
        }),
      );
    }

    result[result.length - 1].last = true;
    await this.scheduleRepository.save(result);
  }

  async getScheduleEntityByScheduleUuid(scheduleUuid: string) {
    return await this.scheduleRepository.findOne({ where: { scheduleUuid }, relations: ["parent"] });
  }

  async getFirstScheduleUuidByMetadataId(metadataId: number) {
    const record = await this.scheduleRepository.findOne({ where: { metadataId } });
    return record.scheduleUuid;
  }

  async checkScheduleSuccessByMetadataIdAndEndAt(metadataId: number, endAt: string) {
    const scheduleEntity = await this.scheduleRepository.findOne({ where: { metadataId, endAt } });
    const result = !scheduleEntity.failed && scheduleEntity.finished;
    return result;
  }

  async checkedSchedule(scheduleUuid: string) {
    const record = await this.scheduleRepository.findOne({ where: { scheduleUuid } });

    if (!record) {
      throw new BadRequestException("해당하는 일정이 없습니다.");
    }

    record.finished = !record.finished;

    const endAt = new Date(record.endAt);
    const now = new Date();
    if (endAt < now) {
      record.failed = true;
    }

    await this.scheduleRepository.save(record);
  }
}
