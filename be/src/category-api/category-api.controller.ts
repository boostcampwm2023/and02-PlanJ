import { Body, Controller, Delete, Get, Param, Patch, Post, Query, UseGuards } from "@nestjs/common";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { CategoryApiService } from "./category-api.service";
import { DeleteCategoryDto } from "src/category/dto/delete-category.dto";
import { AuthGuard } from "../guard/auth.guard";
import { Token } from "../utils/token.decorator";
import { UpdateCategoryDto } from "../category/dto/update-category.dto";

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

  @Patch("/update")
  async update(@Token() token: string, @Body() dto: UpdateCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.update(dto, token);
    return JSON.parse(result);
  }

  @Get("/list")
  async getCategories(@Token() token: string): Promise<string> {
    const result = await this.categoryApiService.getCategories(token);
    return JSON.parse(result);
  }

  @Get()
  async getSchedules(@Token() token: string, @Query("categoryUuid") categoryUuid: string): Promise<JSON> {
    const result = await this.categoryApiService.getSchedules(categoryUuid, token);
    return JSON.parse(result);
  }
}
