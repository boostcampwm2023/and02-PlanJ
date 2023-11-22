import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { CategoryRepository } from "./category.repository";
import { UserEntity } from "src/user/entity/user.entity";
import { AddCategoryDto } from "./dto/add-category.dto";
import { CategoryEntity } from "./entity/category.entity";
import { DeleteCategoryDto } from "./dto/delete-category.dto";

@Injectable()
export class CategoryService {
  constructor(
    @InjectRepository(CategoryRepository)
    private categoryRepository: CategoryRepository,
  ) {}

  async addCategory(dto: AddCategoryDto, user: UserEntity): Promise<string> {
    return await this.categoryRepository.add(dto, user);
  }

  async getCategoryEntity(categoryUuid: string): Promise<CategoryEntity> {
    return await this.categoryRepository.checkByCategoryUuid(categoryUuid);
  }

  async deleteCategory(dto: DeleteCategoryDto) {
    return await this.categoryRepository.deleteCategory(dto);
  }
}
