import { ForbiddenException, Injectable } from "@nestjs/common";
import { CategoryService } from "src/category/category.service";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { DeleteCategoryDto } from "src/category/dto/delete-category.dto";
import { UserService } from "src/user/user.service";
import { AuthService } from "../auth/auth.service";
import { UpdateCategoryDto } from "../category/dto/update-category.dto";
import { HttpResponse } from "../utils/http.response";
import { ScheduleMetaService } from "../schedule/schedule-meta.service";

@Injectable()
export class CategoryApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private authService: AuthService,
    private scheduleMetaService: ScheduleMetaService,
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

  async update(dto: UpdateCategoryDto, token: string): Promise<string> {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);
    dto.userId = userEntity.userId;
    const updatedCategoryName = await this.categoryService.updateCategory(dto);

    const result: HttpResponse = {
      message: "카테고리 업데이트 완료",
      data: {
        updatedCategoryName: updatedCategoryName,
      },
    };

    return JSON.stringify(result);
  }

  async getSchedules(categoryUuid: string, token: string) {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);
    const categoryEntity = await this.categoryService.getCategoryEntity(categoryUuid);

    if (categoryEntity.userId !== userEntity.userId) {
      throw new ForbiddenException("해당 사용자에게 권한이 없습니다.");
    }

    const schedules = await this.scheduleMetaService.getAllScheduleByCategoryId(categoryEntity.categoryId);

    const result: HttpResponse = {
      message: "카테고리내 일정 조회 성공",
      data: schedules,
    };
    return JSON.stringify(result);
  }
}
