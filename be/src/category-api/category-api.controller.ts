import { Body, Controller, Post } from "@nestjs/common";
import { CategoryService } from "src/category/category.service";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { UserEntity } from "src/user/entity/user.entity";

@Controller("/api/category")
export class CategoryApiController {
  constructor(private categoryService: CategoryService) {}

  @Post("/add")
  async add(@Body() dto: AddCategoryDto): Promise<JSON> {
    const result = await this.categoryService.add(dto);
    return JSON.parse(result);
  }
}
