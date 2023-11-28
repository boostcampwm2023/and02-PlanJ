import { Injectable } from "@nestjs/common";
import { RepetitionDto } from "./dto/repetition.dto";
import { CycleType } from "../utils/domain/cycle-type.enum";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { RepetitionEntity } from "./entity/repetition.entity";
import { Repository } from "typeorm";

@Injectable()
export class RepetitionService {
  constructor(
    @InjectRepository(RepetitionEntity)
    private repetitionRepository: Repository<RepetitionEntity>,
  ) {}

  async updateRepetition(dto: UpdateScheduleDto, metadataId: number) {
    let record = await this.repetitionRepository.findOne({ where: { metadataId: metadataId } });
    const repetition: RepetitionDto = dto.repetition;

    // 반복 정보가 null 일 때
    if (!repetition) {
      // record 가 null 일 때
      if (!!record) {
        await this.repetitionRepository.remove(record);
      }
      return;
    }

    if (!record) {
      record = this.repetitionRepository.create({
        metadataId: metadataId,
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
