import { Injectable } from "@nestjs/common";
import { Repository } from "typeorm";
import { RepetitionEntity } from "./entity/repetition.entity";

@Injectable()
export class RepetitionRepository extends Repository<RepetitionEntity> {}
