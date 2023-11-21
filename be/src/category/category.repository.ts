import { Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { CategoryEntity } from "./entity/category.entity";
import { DataSource, Repository } from "typeorm";
import { AddCategoryDto } from "./dto/add-category.dto";
import { ulid } from "ulid";
import { HttpResponse } from "src/utils/http.response";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class CategoryRepository extends Repository<CategoryEntity> {
  constructor(dataSource: DataSource) {
    super(CategoryEntity, dataSource.createEntityManager());
  }

  async add(dto: AddCategoryDto, user: UserEntity): Promise<string> {
    const { categoryName, createdAt } = dto;

    const categoryUuid = ulid();

    const category = this.create({
      categoryUuid: categoryUuid,
      categoryName: categoryName,
      createdAt: createdAt,
      user: user,
    });

    console.log(category);

    try {
      await this.save(category);
      const body: HttpResponse = {
        message: "카테고리 생성",
        statusCode: 201,
        data: {
          categoryUuid: categoryUuid,
        },
      };

      return JSON.stringify(body);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }

  async checkByCategoryUuid(categoryUuid: string): Promise<CategoryEntity> {
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
