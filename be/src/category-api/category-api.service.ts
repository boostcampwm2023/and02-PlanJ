import { Injectable } from "@nestjs/common";
import { AddCategoryService } from "src/category/add-category.service";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { GetUserEntityService } from "src/category/get-user-entity.service";

@Injectable()
export class CategoryApiService {
  constructor(
    private getUserEntityService: GetUserEntityService,
    private addCategoryService: AddCategoryService,
  ) {}

  async add(dto: AddCategoryDto): Promise<string> {
    const user = await this.getUserEntityService.getUserEntity(dto);
    return await this.addCategoryService.addCategory(dto, user);
  }
}
