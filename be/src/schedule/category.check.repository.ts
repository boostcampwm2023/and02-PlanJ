import { Injectable, NotFoundException } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { AddScheduleDto } from "./dto/add-schedule.dto";
import { CategoryEntity } from "src/category/entity/category.entity";

@Injectable()
export class CategoryCheckRepository extends Repository<CategoryEntity> {
  constructor(dataSource: DataSource) {
    super(CategoryEntity, dataSource.createEntityManager());
  }

  async checkByCategoryUuid(dto: AddScheduleDto): Promise<CategoryEntity> {
    const { categoryUuid } = dto;
    return await this.getCategoryEntity(categoryUuid);
  }

  private async getCategoryEntity(categoryUuid: string) {
    const category = await this.findOne({
      where: { categoryUuid: categoryUuid },
    });

    if (category === null) {
      throw new NotFoundException("존재하지 않는 category uuid");
    }
    return category;
  }
}
