import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { Repository, DataSource } from "typeorm";
import { ParticipantEntity } from "./entity/participant.entity";

@Injectable()
export class ParticipateRepository extends Repository<ParticipantEntity> {
  constructor(dataSource: DataSource) {
    super(ParticipantEntity, dataSource.createEntityManager());
  }

  async invite(authorMetadataId: number, invitedMetadataId: number): Promise<void> {
    if (await this.isNotMade(authorMetadataId)) {
      const authorRecord = this.create({ participantPeopleId: authorMetadataId, authorId: authorMetadataId });
      await this.save(authorRecord);
    }

    const participantRecord = this.create({ participantPeopleId: invitedMetadataId, authorId: authorMetadataId });

    try {
      await this.save(participantRecord);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  // 만들어진 적 없으면 true
  private async isNotMade(authorMetadataId: number) {
    return (await this.findOne({ where: { authorId: authorMetadataId } })) === null;
  }
}
