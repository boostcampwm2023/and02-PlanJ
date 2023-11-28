import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { ScheduleLocationEntity } from "./entity/schedule-location.entity";
import { ScheduleMetadataEntity } from "./entity/schedule-metadata.entity";
import { ScheduleLocationDto } from "src/schedule/dto/schedule-location.dto";

@Injectable()
export class ScheduleLocationRepository extends Repository<ScheduleLocationEntity> {
  constructor(dataSource: DataSource) {
    super(ScheduleLocationEntity, dataSource.createEntityManager());
  }
}
