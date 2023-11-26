import { Body, Controller, Delete, Param, Post, UseGuards } from "@nestjs/common";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { CategoryApiService } from "./category-api.service";
import { DeleteCategoryDto } from "src/category/dto/delete-category.dto";
import { AuthGuard } from "../guard/auth.guard";
import { Token } from "../utils/token.decorator";

@Controller("/api/category")
@UseGuards(AuthGuard)
export class CategoryApiController {
  constructor(private categoryApiService: CategoryApiService) {}

  @Post("/add")
  async add(@Token() token: string, @Body() dto: AddCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.add(dto, token);
    return JSON.parse(result);
  }

  @Delete("/delete/:categoryUuid")
  async delete(@Token() token: string, @Param() dto: DeleteCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.delete(dto, token);
    return JSON.parse(result);
  }
}
