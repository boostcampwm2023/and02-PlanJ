import { Injectable, NotFoundException } from "@nestjs/common";
import { CategoryEntity } from "./entity/category.entity";
import { DataSource, Repository } from "typeorm";

@Injectable()
export class CategoryRepository extends Repository<CategoryEntity> {
  constructor(dataSource: DataSource) {
    super(CategoryEntity, dataSource.createEntityManager());
  }

  async findOneByCategoryUuid(categoryUuid: string) {
    const category = await this.findOne({
      where: { categoryUuid: categoryUuid },
    });

    if (category === null) {
      throw new NotFoundException("존재하지 않는 category uuid");
    }
    return category;
  }

  async findByUserId(userId: number) {
    return await this.createQueryBuilder("category")
      .select(["category.categoryUuid", "category.categoryName"])
      .andWhere("category.user_id = :userId", { userId: userId })
      .orderBy("category.created_at")
      .getMany();
  }
}
