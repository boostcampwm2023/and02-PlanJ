import { Injectable } from "@nestjs/common";
import { CategoryEntity } from "./entity/category.entity";
import { DataSource, Repository } from "typeorm";

@Injectable()
export class CategoryRepository extends Repository<CategoryEntity> {
  constructor(dataSource: DataSource) {
    super(CategoryEntity, dataSource.createEntityManager());
  }

  async findOneByCategoryUuid(categoryUuid: string) {
    if (categoryUuid === "default") {
      return null;
    }

    return await this.findOne({ where: { categoryUuid: categoryUuid } });
  }

  async findByUserId(userId: number) {
    return await this.createQueryBuilder("category")
      .select(["category.categoryUuid", "category.categoryName"])
      .andWhere("category.user_id = :userId", { userId: userId })
      .orderBy("category.created_at")
      .getMany();
  }
}
