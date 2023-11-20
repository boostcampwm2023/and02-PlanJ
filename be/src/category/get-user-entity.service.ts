import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { UserCheckRepository } from "./user.check.repository";
import { UserEntity } from "src/user/entity/user.entity";
import { AddCategoryDto } from "./dto/add-category.dto";

@Injectable()
export class GetUserEntityService {
  constructor(
    @InjectRepository(UserCheckRepository)
    private userCheckRepository: UserCheckRepository,
  ) {}

  async getUserEntity(dto: AddCategoryDto): Promise<UserEntity> {
    return await this.userCheckRepository.checkByUserUuid(dto);
  }
}
