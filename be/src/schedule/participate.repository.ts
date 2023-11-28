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

  async addDefaultParticipantGroup(user: UserEntity, scheduleMeta: ScheduleMetadataEntity): Promise<void> {
    const record = this.create({ author: true, user, scheduleMeta });

    console.log(record);

    try {
      await this.save(record);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }
}
