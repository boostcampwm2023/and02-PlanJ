import { Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { CategoryEntity } from "./entity/category.entity";
import { DataSource, Repository } from "typeorm";
import { AddCategoryDto } from "./dto/add-category.dto";
import { ulid } from "ulid";
import { HttpResponse } from "src/utils/http.response";
import { UserEntity } from "src/user/entity/user.entity";
import { DeleteCategoryDto } from "./dto/delete-category.dto";

@Injectable()
export class CategoryRepository extends Repository<CategoryEntity> {
  constructor(dataSource: DataSource) {
    super(CategoryEntity, dataSource.createEntityManager());
  }

  async findOneByCategoryUuid(categoryUuid: string) {
    if (categoryUuid === "default") {
      return null;
    }

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
