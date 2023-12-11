import { Body, Controller, Delete, Get, Param, Patch, Post, Query, UseGuards } from "@nestjs/common";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { CategoryApiService } from "./category-api.service";
import { DeleteCategoryDto } from "src/category/dto/delete-category.dto";
import { AuthGuard } from "../guard/auth.guard";
import { Token } from "../utils/token.decorator";
import { UpdateCategoryDto } from "../category/dto/update-category.dto";
import { ApiBearerAuth, ApiOperation, ApiTags, ApiUnauthorizedResponse } from "@nestjs/swagger";

@ApiTags("카테고리")
@Controller("/api/category")
@ApiBearerAuth("access-token")
@ApiUnauthorizedResponse({
  description: "유효한 토큰이 아닐 때",
  schema: {
    example: {
      message: "유효하지 않은 사용자입니다.",
      error: "Unauthorized",
      statusCode: 401,
    },
  },
})
@UseGuards(AuthGuard)
export class CategoryApiController {
  constructor(private categoryApiService: CategoryApiService) {}

  @ApiOperation({ summary: "카테고리 추가" })
  @Post("/add")
  async add(@Token() token: string, @Body() dto: AddCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.add(dto, token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "카테고리 삭제" })
  @Delete("/delete/:categoryUuid")
  async delete(@Token() token: string, @Param() dto: DeleteCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.delete(dto, token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "카테고리 업데이트" })
  @Patch("/update")
  async update(@Token() token: string, @Body() dto: UpdateCategoryDto): Promise<JSON> {
    const result = await this.categoryApiService.update(dto, token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "카테고리 목록 조회" })
  @Get("/list")
  async getCategories(@Token() token: string): Promise<string> {
    const result = await this.categoryApiService.getCategories(token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "카테고리 내의 일정 조회" })
  @Get()
  async getSchedules(@Token() token: string, @Query("categoryUuid") categoryUuid: string): Promise<JSON> {
    const result = await this.categoryApiService.getSchedules(categoryUuid, token);
    return JSON.parse(result);
  }
}
