import { Body, Controller, Delete, Header, Post } from "@nestjs/common";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { CategoryApiService } from "./category-api.service";
import { DeleteCategoryDto } from "src/category/dto/delete-category.dto";

@Controller("/api/category")
export class CategoryApiController {
  constructor(private categoryApiService: CategoryApiService) {}

  @Post("/add")
  // @Header("content-type", "application/json")
  async add(@Body() dto: AddCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.add(dto);
    return JSON.parse(result);
  }

  @Delete("/delete")
  async delete(@Body() dto: DeleteCategoryDto) {
    const result = await this.categoryApiService.delete(dto);
    return JSON.parse(result);
  }
}
