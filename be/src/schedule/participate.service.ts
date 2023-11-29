import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ParticipateRepository } from "./participate.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";

@Injectable()
export class ParticipateService {
  constructor(
    @InjectRepository(ParticipateRepository)
    private participateRepository: ParticipateRepository,
  ) {}

  async inviteSchedule(authorScheduleMetadata: ScheduleMetadataEntity, invitedMetadataId: number) {
    await this.participateRepository.invite(authorScheduleMetadata, invitedMetadataId);
  }
}
