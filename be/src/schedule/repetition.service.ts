import { Injectable } from "@nestjs/common";
import { RepetitionDto } from "./dto/repetition.dto";
import { CycleType } from "../utils/domain/cycle-type.enum";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { RepetitionEntity } from "./entity/repetition.entity";
import { Repository } from "typeorm";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";

@Injectable()
export class RepetitionService {
  constructor(
    @InjectRepository(RepetitionEntity)
    private repetitionRepository: Repository<RepetitionEntity>,
  ) {}

  async updateRepetition(dto: UpdateScheduleDto, scheduleMeta: ScheduleMetadataEntity) {
    let record = await this.repetitionRepository.findOne({ where: { metadataId: scheduleMeta.metadataId } });
    const repetition: RepetitionDto = dto.repetition;

    // 반복 정보가 null 일 때
    if (!repetition) {
      // record 가 null 이 아닐 때
      if (!!record) {
        await this.repetitionRepository.remove(record);
      }
      return;
    }

    if (!record) {
      record = this.repetitionRepository.create({
        metadataId: scheduleMeta.metadataId,
        cycleType: CycleType[repetition.cycleType],
        cycleCount: repetition.cycleCount,
      });
    } else {
      record.cycleType = CycleType[repetition.cycleType];
      record.cycleCount = repetition.cycleCount;
    }

    await this.repetitionRepository.save(record);
  }
}
