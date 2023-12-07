import { Injectable } from "@nestjs/common";
import { RepetitionDto } from "./dto/repetition.dto";
import { CycleType } from "../utils/domain/cycle-type.enum";
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

  async updateRepetition(repetitionDto: RepetitionDto, scheduleMeta: ScheduleMetadataEntity): Promise<boolean> {
    let record = await this.repetitionRepository.findOne({ where: { metadataId: scheduleMeta.metadataId } });
    const repetition: RepetitionDto = repetitionDto;

    // 반복 정보가 null 일 때
    if (!repetition) {
      // record 가 null 이 아닐 때
      if (!!record) {
        await this.repetitionRepository.remove(record);
        return true;
      }
      return false;
    }

    if (!record) {
      record = this.repetitionRepository.create({
        metadataId: scheduleMeta.metadataId,
        cycleType: CycleType[repetition.cycleType],
        cycleCount: repetition.cycleCount,
      });
    } else {
      // 반복 정보 비교후 같으면 종료
      if (this.compareRepetitionInfo(record, repetition)) {
        return false;
      }
      record.cycleType = CycleType[repetition.cycleType];
      record.cycleCount = repetition.cycleCount;
    }

    await this.repetitionRepository.save(record);
    return true;
  }

  async getRepetitionByMetadataId(metadataId: number) {
    return await this.repetitionRepository.findOne({ where: { metadataId } });
  }

  private compareRepetitionInfo(record: RepetitionEntity, repetition: RepetitionDto) {
    return record.cycleType === repetition.cycleType && record.cycleCount === repetition.cycleCount;
  }
}
