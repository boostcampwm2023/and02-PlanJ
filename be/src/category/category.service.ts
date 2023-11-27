import { ForbiddenException, Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { CategoryRepository } from "./category.repository";
import { UserEntity } from "src/user/entity/user.entity";
import { AddCategoryDto } from "./dto/add-category.dto";
import { CategoryEntity } from "./entity/category.entity";
import { DeleteCategoryDto } from "./dto/delete-category.dto";
import { UpdateCategoryDto } from "./dto/update-category.dto";

@Injectable()
export class CategoryService {
  constructor(
    @InjectRepository(CategoryRepository)
    private categoryRepository: CategoryRepository,
  ) {}

  async addCategory(dto: AddCategoryDto, user: UserEntity): Promise<string> {
    return await this.categoryRepository.add(dto, user);
  }

  async getCategoryEntity(categoryUuid: string): Promise<CategoryEntity | null> {
    return await this.categoryRepository.checkByCategoryUuid(categoryUuid);
  }

  async deleteCategory(dto: DeleteCategoryDto) {
    return await this.categoryRepository.deleteCategory(dto);
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
}
