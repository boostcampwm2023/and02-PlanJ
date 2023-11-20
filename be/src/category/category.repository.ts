import { Injectable, InternalServerErrorException } from "@nestjs/common";
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
}
