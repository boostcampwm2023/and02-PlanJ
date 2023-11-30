import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ParticipateRepository } from "./participate.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ParticipantEntity } from "./entity/participant.entity";

@Injectable()
export class ParticipateService {
  constructor(
    @InjectRepository(ParticipateRepository)
    private participateRepository: ParticipateRepository,
  ) {}

  async inviteSchedule(authorScheduleMetadata: ScheduleMetadataEntity, invitedMetadataId: number) {
    await this.participateRepository.invite(authorScheduleMetadata, invitedMetadataId);
  }

  async isAlreadyInvited(authorMetadataId: number, invitedUserId: number): Promise<number[]> {
    return await this.participateRepository.isAlreadyInvited(authorMetadataId, invitedUserId);
  }

  async getParticipantGroup(metadataId: number): Promise<ParticipantEntity[]> {
    const record = await this.participateRepository.findOne({ where: { participantId: metadataId } });
    if (record === null) {
      return null;
    }

    return await this.participateRepository.find({ where: { authorId: record.authorId } });
  }
}
