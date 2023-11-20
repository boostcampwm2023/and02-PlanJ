import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { CategoryCheckRepository } from "./category.check.repository";
import { CategoryEntity } from "src/category/entity/category.entity";

@Injectable()
export class GetCategoryEntityService {
  constructor(
    @InjectRepository(CategoryCheckRepository)
    private categoryCheckRepository: CategoryCheckRepository,
  ) {}

  async getCategoryEntity(dto: AddScheduleDto): Promise<CategoryEntity> {
    return await this.categoryCheckRepository.checkByCategoryUuid(dto);
  }
}
