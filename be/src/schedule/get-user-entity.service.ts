import { Injectable } from "@nestjs/common";
import { UserCheckRepository } from "./user.check.repository";
import { InjectRepository } from "@nestjs/typeorm";
import { UserEntity } from "src/user/entity/user.entity";
import { AddScheduleDto } from "./dto/add-schedule.dto";

@Injectable()
export class GetUserEntityService {
  constructor(
    @InjectRepository(UserCheckRepository)
    private userCheckRepository: UserCheckRepository,
  ) {}

  async getUserEntity(dto: AddScheduleDto): Promise<UserEntity> {
    return await this.userCheckRepository.checkByUserUuid(dto);
  }
}
