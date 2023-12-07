import { InviteStatus } from "../utils/domain/invite-status.enum";
import { Injectable, InternalServerErrorException, Logger } from "@nestjs/common";
import { Repository, DataSource } from "typeorm";
import { ParticipantEntity } from "./entity/participant.entity";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class ParticipateRepository extends Repository<ParticipantEntity> {
  private readonly logger = new Logger(ParticipateRepository.name);
  constructor(dataSource: DataSource) {
    super(ParticipantEntity, dataSource.createEntityManager());
  }

  async invite(authorScheduleMetadata: ScheduleMetadataEntity, invitedMetadataId: number): Promise<void> {
    const authorId = authorScheduleMetadata.metadataId;
    const isNotMade = await this.isNotMade(authorScheduleMetadata);

    if (isNotMade) {
      const authorRecord = this.create({ participantId: authorId, authorId });
      await this.save(authorRecord);
    }

    const participantRecord = this.create({ participantId: invitedMetadataId, authorId });

    try {
      await this.save(participantRecord);
      return;
    } catch (error) {
      this.logger.error(error);
      throw new InternalServerErrorException();
    }
  }

  // 만들어진 적 없으면 true
  private async isNotMade(authorScheduleMetadata: ScheduleMetadataEntity) {
    const count = await this.count({ where: { authorId: authorScheduleMetadata.metadataId } });
    return count === 0;
  }

  async unInvite(authorMetadataId: number, invitedMetadataId: number) {
    try {
      await this.softDelete({ authorId: authorMetadataId, participantId: invitedMetadataId });
    } catch (error) {
      this.logger.error(error);
      throw new InternalServerErrorException();
    }
  }

  async getInvitedMetadataId(authorMetadataId: number, invitedUserId: number): Promise<number> {
    const records = await this.createQueryBuilder("participant")
      .leftJoinAndSelect("participant.participant", "schedule_metadata")
      .where("participant.author_id = :authorId", { authorId: authorMetadataId })
      .where("schedule_metadata.user_id = :userId", { userId: invitedUserId })
      .getMany();

    return records[0].participantId;
  }

  async checkInvitedStatus(
    authorMetadata: ScheduleMetadataEntity,
    groupUserEntities: UserEntity[],
    invitedUserEntities: UserEntity[],
  ) {
    const invitedStatus = {};

    for (const participant of groupUserEntities) {
      if (participant.userId === authorMetadata.userId) {
        continue;
      }
      invitedStatus[participant.email] = InviteStatus.DELETED;
    }

    for (const invited of invitedUserEntities) {
      invitedStatus[invited.email] = InviteStatus.NEW;
    }

    for (const participant of groupUserEntities) {
      for (const invited of invitedUserEntities) {
        if (participant.email === invited.email) {
          invitedStatus[participant.email] = InviteStatus.CHANGED;
        }
      }
    }

    this.logger.verbose("Invited Status: " + JSON.stringify(invitedStatus, null, 2));
    const invitedStatusArray: [string, InviteStatus][] = Object.entries(invitedStatus);
    return invitedStatusArray;
  }
}
