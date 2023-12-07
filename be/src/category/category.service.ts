import { ForbiddenException, Injectable, InternalServerErrorException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { CategoryRepository } from "./category.repository";
import { UserEntity } from "src/user/entity/user.entity";
import { AddCategoryDto } from "./dto/add-category.dto";
import { CategoryEntity } from "./entity/category.entity";
import { DeleteCategoryDto } from "./dto/delete-category.dto";
import { UpdateCategoryDto } from "./dto/update-category.dto";
import { ulid } from "ulid";

@Injectable()
export class CategoryService {
  constructor(
    @InjectRepository(CategoryRepository)
    private categoryRepository: CategoryRepository,
  ) {}

  async addCategory(dto: AddCategoryDto, user: UserEntity): Promise<string> {
    const { categoryName } = dto;
    const categoryUuid = ulid();
    const category = this.categoryRepository.create({
      categoryUuid: categoryUuid,
      categoryName: categoryName,
      user: user,
    });

    try {
      await this.categoryRepository.save(category);
      return categoryUuid;
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }

  async getCategoryEntity(categoryUuid: string): Promise<CategoryEntity> {
    return await this.categoryRepository.findOneByCategoryUuid(categoryUuid);
  }

  async getCategoryEntityByCategoryId(categoryId: number): Promise<CategoryEntity> {
    if (!categoryId) {
      return null;
    }
    return await this.categoryRepository.findOne({ where: { categoryId } });
  }

  async deleteCategory(dto: DeleteCategoryDto) {
    const { categoryUuid } = dto;

    try {
      const record = await this.categoryRepository.findOne({ where: { categoryUuid }, relations: ["scheduleMeta"] });
      record.scheduleMeta.forEach((entity) => {
        entity.softRemove();
      });

      await this.categoryRepository.softDelete({ categoryUuid });
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  async updateCategory(dto: UpdateCategoryDto) {
    const { userId, categoryUuid, categoryName } = dto;
    const categoryEntity = await this.categoryRepository.findOne({ where: { categoryUuid: categoryUuid } });

    if (categoryEntity.userId !== userId) {
      throw new ForbiddenException("해당 사용자에게 권한이 없습니다.");
    }

    if (categoryEntity.categoryName !== categoryName) {
      categoryEntity.categoryName = categoryName;
      await this.categoryRepository.save(categoryEntity);
    }

    return categoryName;
  }

  async getCategories(userId: number) {
    return await this.categoryRepository.findByUserId(userId);
  }
}
