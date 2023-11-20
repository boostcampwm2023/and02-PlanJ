import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { CategoryRepository } from "./category.repository";
import { UserEntity } from "src/user/entity/user.entity";
import { AddCategoryDto } from "./dto/add-category.dto";

@Injectable()
export class AddCategoryService {
  constructor(
    @InjectRepository(CategoryRepository)
    private categoryRepository: CategoryRepository,
  ) {}

  async addCategory(dto: AddCategoryDto, user: UserEntity): Promise<string> {
    return await this.categoryRepository.add(dto, user);
  }
}
