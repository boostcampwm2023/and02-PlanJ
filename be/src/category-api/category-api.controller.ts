import { Body, Controller, Post } from "@nestjs/common";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { CategoryApiService } from "./category-api.service";

@Controller("/api/category")
export class CategoryApiController {
  constructor(private categoryApiService: CategoryApiService) {}

  @Post("/add")
  async add(@Body() dto: AddCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.add(dto);
    return JSON.parse(result);
  }
}
