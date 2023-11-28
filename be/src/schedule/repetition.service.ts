import { Injectable } from "@nestjs/common";
import { RepetitionRepository } from "./repetition.repository";
import { RepetitionDto } from "./dto/repetition.dto";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { CycleType } from "../utils/domain/cycle-type.enum";
import { InjectRepository } from "@nestjs/typeorm";

@Injectable()
export class RepetitionService {
  constructor(private repetitionRepository: RepetitionRepository) {}

  async addRepetition(dto: AddScheduleDto, metadataId: number) {
    const repetition: RepetitionDto = dto.repetition;
    const repetitionEntity = this.repetitionRepository.create({
      metadataId: metadataId,
      cycleType: CycleType[repetition.cycleType],
      cycleCount: repetition.cycleCount,
    });

    await this.repetitionRepository.save(repetitionEntity);
  }
}
