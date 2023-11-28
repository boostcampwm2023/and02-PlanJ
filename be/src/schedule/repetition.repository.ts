import { Injectable } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { RepetitionEntity } from "./entity/repetition.entity";

@Injectable()
export class RepetitionRepository extends Repository<RepetitionEntity> {
  constructor(dataSource: DataSource) {
    super(RepetitionEntity, dataSource.createEntityManager());
  }
}
