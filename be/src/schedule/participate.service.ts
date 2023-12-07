import { Injectable, Logger } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { ParticipateRepository } from "./participate.repository";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ParticipantEntity } from "./entity/participant.entity";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class ParticipateService {
  private readonly logger = new Logger(ParticipateService.name);
  constructor(
    @InjectRepository(ParticipateRepository)
    private participateRepository: ParticipateRepository,
  ) {}

  async inviteSchedule(authorScheduleMetadata: ScheduleMetadataEntity, invitedMetadataId: number) {
    await this.participateRepository.invite(authorScheduleMetadata, invitedMetadataId);
  }

  async unInviteSchedule(authorMetadataId: number, invitedMetadataId: number) {
    await this.participateRepository.unInvite(authorMetadataId, invitedMetadataId);
  }

  async getInvitedMetadataId(authorMetadataId: number, invitedUserId: number): Promise<number> {
    return await this.participateRepository.getInvitedMetadataId(authorMetadataId, invitedUserId);
  }

  async getParticipantGroup(metadataId: number): Promise<ParticipantEntity[]> {
    const record = await this.participateRepository.findOne({ where: { participantId: metadataId } });
    if (record === null) {
      return null;
    }

    return await this.participateRepository.find({ where: { authorId: record.authorId } });
  }

  async getAuthorGroup(authorMetadataId: number) {
    return await this.participateRepository
      .createQueryBuilder("participant")
      .leftJoinAndSelect("participant.participant", "schedule_metadata")
      .where("participant.author_id = :authorId", { authorId: authorMetadataId })
      .getMany();
  }

  async checkInvitedStatus(
    authorMetadata: ScheduleMetadataEntity,
    groupUserEntities: UserEntity[],
    invitedUserEntities: UserEntity[],
  ) {
    return await this.participateRepository.checkInvitedStatus(authorMetadata, groupUserEntities, invitedUserEntities);
  }

  async deleteAuthor(authorMetadataId: number) {
    await this.participateRepository.softDelete({ authorId: authorMetadataId, participantId: authorMetadataId });
  }

  async deleteGroup(authorMetadataId: number) {
    await this.participateRepository.softDelete({ authorId: authorMetadataId });
  }

  async checkIsAuthor(metadataId: number) {
    const participantEntity = await this.participateRepository.findOne({ where: { participantId: metadataId } });
    return participantEntity === null || participantEntity.participantId === participantEntity.authorId;
  }
}
