import { Injectable } from "@nestjs/common";
import { CategoryService } from "src/category/category.service";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { DeleteCategoryDto } from "src/category/dto/delete-category.dto";
import { UserService } from "src/user/user.service";
import { AuthService } from "../auth/auth.service";

@Injectable()
export class CategoryApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private authService: AuthService,
  ) {}

  async add(dto: AddCategoryDto, token: string): Promise<string> {
    dto.userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(dto.userUuid);
    return await this.categoryService.addCategory(dto, user);
  }

  async delete(dto: DeleteCategoryDto, token: string) {
    dto.userUuid = this.authService.verify(token);
    return await this.categoryService.deleteCategory(dto);
  }
}
