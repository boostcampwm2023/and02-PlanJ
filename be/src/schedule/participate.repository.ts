import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { Repository, DataSource } from "typeorm";
import { ParticipantEntity } from "./entity/participant.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";

@Injectable()
export class ParticipateRepository extends Repository<ParticipantEntity> {
  constructor(dataSource: DataSource) {
    super(ParticipantEntity, dataSource.createEntityManager());
  }

  async invite(authorMetadataId: number, invitedMetadataId: number): Promise<void> {
    if (this.isNotMade(authorMetadataId)) {
      const record = this.create({ participantId: authorMetadataId, authorId: authorMetadataId });
      this.save(record);
    }

    const record = this.create({ participantId: invitedMetadataId, authorId: authorMetadataId });

    try {
      await this.save(record);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  // 만들어진 적 없으면 true
  private isNotMade(authorMetadataId: number) {
    return !this.findOne({ where: { authorId: authorMetadataId } });
  }
}
