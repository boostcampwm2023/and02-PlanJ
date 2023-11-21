import { Injectable } from "@nestjs/common";
import { CategoryService } from "src/category/category.service";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { UserService } from "src/user/user.service";

@Injectable()
export class CategoryApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
  ) {}

  async add(dto: AddCategoryDto): Promise<string> {
    const user = await this.userService.getUserEntity(dto.userUuid);
    return await this.categoryService.addCategory(dto, user);
  }
}
