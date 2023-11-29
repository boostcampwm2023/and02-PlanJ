import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { Repository, DataSource } from "typeorm";
import { ParticipantEntity } from "./entity/participant.entity";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";

@Injectable()
export class ParticipateRepository extends Repository<ParticipantEntity> {
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
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  // 만들어진 적 없으면 true
  private async isNotMade(authorScheduleMetadata: ScheduleMetadataEntity) {
    const count = await this.count({ where: { authorId: authorScheduleMetadata.metadataId } });
    return count === 0;
  }

  // scheduleUuid에 연결된 schedulemetadataid가 이미 participant테이블의 author에 있는지 체크
  // 초대된 사용자가 새롭게 초대된 것인지 체크
  // 이미 있고 새롭게 초대된 것이 아니라면 add 필요없고 participant 테이블에 새로 만들 필요 없음
  async isAlreadyInvited(authorMetadataId: number, invitedUserId: number): Promise<(number | boolean)[]> {
    const records = await this.createQueryBuilder("participant")
      .leftJoinAndSelect("participant.participant", "schedule_metadata")
      .where("participant.author_id = :authorId", { authorId: authorMetadataId })
      .where("schedule_metadata.user_id = :userId", { userId: invitedUserId })
      .getMany();

    console.log(records);
    if (records.length === 1) {
      return [true, records[0].participantId];
    }

    if (records.length === 0) {
      return [false, null];
    }
  }
}
