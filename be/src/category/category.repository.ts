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

  async findByUserId(userId: number) {
    return await this.createQueryBuilder("category")
      .select(["category.categoryUuid", "category.categoryName"])
      .andWhere("category.user_id = :userId", { userId: userId })
      .orderBy("category.created_at")
      .getMany();
  }
}
