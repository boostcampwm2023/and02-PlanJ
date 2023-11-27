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

  async add(dto: AddCategoryDto, user: UserEntity): Promise<string> {
    const { categoryName } = dto;

    const categoryUuid = ulid();

    const category = this.create({
      categoryUuid: categoryUuid,
      categoryName: categoryName,
      user: user,
    });

    try {
      await this.save(category);
      const body: HttpResponse = {
        message: "카테고리 생성",
        data: {
          categoryUuid: categoryUuid,
        },
      };

      return JSON.stringify(body);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }

  async checkByCategoryUuid(categoryUuid: string): Promise<CategoryEntity | null> {
    return await this.getCategoryEntity(categoryUuid);
  }

  private async getCategoryEntity(categoryUuid: string) {
    if (categoryUuid === "default") {
      return null;
    }

    const category = await this.findOne({
      where: { categoryUuid },
    });

    return category;
  }

  async deleteCategory(dto: DeleteCategoryDto) {
    const { categoryUuid } = dto;

    const record = await this.findOne({ where: { categoryUuid }, relations: ["scheduleMeta"] });

    record.scheduleMeta.forEach((entity) => {
      entity.softRemove();
    });

    await this.softDelete({ categoryUuid });

    const body: HttpResponse = {
      message: "카테고리 삭제 완료",
    };

    return JSON.stringify(body);
  }
}
