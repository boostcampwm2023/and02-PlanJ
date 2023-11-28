import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ParticipateRepository } from "./participate.repository";

@Injectable()
export class ParticipateService {
  constructor(
    @InjectRepository(ParticipateRepository)
    private participateRepository: ParticipateRepository,
  ) {}

  async inviteSchedule(authorMetadataId: number, invitedMetadataId: number) {
    await this.participateRepository.invite(authorMetadataId, invitedMetadataId);
  }
}
