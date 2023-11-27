import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { DeleteScheduleDto } from "./dto/delete-schedule.dto";
import { UpdateScheduleDto } from "./dto/update-schedule.dto";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleRepository } from "./schedule.repository";
import { ParticipateRepository } from "./participate.repository";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class ParticipateService {
  constructor(
    @InjectRepository(ParticipateRepository)
    private participateRepository: ParticipateRepository,
  ) {}

  async addDefaultParticipantGroup(user: UserEntity, scheduleMeta: ScheduleMetadataEntity) {
    await this.participateRepository.addDefaultParticipantGroup(user, scheduleMeta);
  }
}
