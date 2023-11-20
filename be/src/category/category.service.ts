import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AddCategoryDto } from "./dto/add-category.dto";
import { CategoryRepository } from "./category.repository";
import { UserCheckRepository } from "./user.check.repository";
import { UserEntity } from "src/user/entity/user.entity";

@Injectable()
export class CategoryService {
  constructor(
    @InjectRepository(CategoryRepository)
    private categoryRepository: CategoryRepository,

    @InjectRepository(UserCheckRepository)
    private userCheckRepository: UserCheckRepository,
  ) {}

  async add(dto: AddCategoryDto): Promise<string> {
    const user = await this.userCheckRepository.checkByUserUuid(dto);
    return await this.categoryRepository.add(dto, user);
  }
}
