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
}
