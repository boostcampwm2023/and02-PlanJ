import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ScheduleAlarmEntity } from "./entity/schedule-alarm.entity";
import { Repository } from "typeorm";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { AlarmType } from "../utils/domain/alarm-type.enum";

@Injectable()
export class ScheduleAlarmService {
  constructor(
    @InjectRepository(ScheduleAlarmEntity) private scheduleAlarmRepository: Repository<ScheduleAlarmEntity>,
  ) {}

  async updateScheduleAlarm(dto: UpdateScheduleDto, metadata: ScheduleMetadataEntity) {
    let record = await this.scheduleAlarmRepository.findOne({ where: { metadataId: metadata.metadataId } });
    const alarmDto = dto.alarm;

    // end location 이 null 일 때
    if (!dto.alarm) {
      // record 가 존재할 때
      if (!!record) {
        await this.scheduleAlarmRepository.remove(record);
      }
      return;
    }

    if (!record) {
      record = this.scheduleAlarmRepository.create({
        alarmType: AlarmType[alarmDto.alarmType],
        alarmTime: alarmDto.alarmTime,
        estimatedTime: alarmDto.estimatedTime ?? 0,
        metadataId: metadata.metadataId,
      });
    } else {
      record.alarmTime = alarmDto.alarmTime;
      record.alarmType = AlarmType[alarmDto.alarmType];
      record.estimatedTime = alarmDto.estimatedTime ?? 0;
    }

    try {
      await this.scheduleAlarmRepository.save(record);
      return;
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  async getAlarmByMetadataId(metadataId: number) {
    return await this.scheduleAlarmRepository.findOne({ where: { metadataId } });
  }
}
