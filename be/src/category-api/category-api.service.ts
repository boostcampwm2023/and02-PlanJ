import { ForbiddenException, Injectable } from "@nestjs/common";
import { CategoryService } from "src/category/category.service";
import { AddCategoryDto } from "src/category/dto/add-category.dto";
import { DeleteCategoryDto } from "src/category/dto/delete-category.dto";
import { UserService } from "src/user/user.service";
import { AuthService } from "../auth/auth.service";
import { UpdateCategoryDto } from "../category/dto/update-category.dto";
import { HttpResponse } from "../utils/http.response";
import { ScheduleMetaService } from "../schedule/schedule-meta.service";
import { ScheduleResponse } from "../schedule/dto/schedule.response";
import { ScheduleEntity } from "../schedule/entity/schedule.entity";
import { ScheduleService } from "../schedule/schedule.service";

@Injectable()
export class CategoryApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private authService: AuthService,
    private scheduleMetaService: ScheduleMetaService,
    private scheduleService: ScheduleService,
  ) {}

  async add(dto: AddCategoryDto, token: string): Promise<string> {
    dto.userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(dto.userUuid);
    const categoryUuid = await this.categoryService.addCategory(dto, user);
    const body: HttpResponse = {
      message: "카테고리 생성",
      data: {
        categoryUuid: categoryUuid,
      },
    };

    return JSON.stringify(body);
  }

  async delete(dto: DeleteCategoryDto, token: string) {
    dto.userUuid = this.authService.verify(token);
    await this.categoryService.deleteCategory(dto);
    const body: HttpResponse = {
      message: "카테고리 삭제 완료",
    };

    return JSON.stringify(body);
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
    let schedules: ScheduleResponse[], updatedSchedules: ScheduleEntity[];

    if (categoryUuid !== "default") {
      const categoryEntity = await this.categoryService.getCategoryEntity(categoryUuid);

      if (categoryEntity.userId !== userEntity.userId) {
        throw new ForbiddenException("해당 사용자에게 권한이 없습니다.");
      }

      [schedules, updatedSchedules] = await this.scheduleMetaService.getAllScheduleByCategoryId(
        categoryEntity.categoryId,
        userEntity.userId,
      );
    } else {
      [schedules, updatedSchedules] = await this.scheduleMetaService.getAllScheduleByNullCategory(userEntity.userId);
    }

    await this.scheduleService.updateScheduleEntities(updatedSchedules);
    schedules.sort((a, b) => a.endAt.localeCompare(b.endAt));

    const result: HttpResponse = {
      message: "카테고리내 일정 조회 성공",
      data: schedules,
    };
    return JSON.stringify(result);
  }

  async getCategories(token: string) {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);
    const categories = await this.categoryService.getCategories(userEntity.userId);

    const result: HttpResponse = {
      message: "사용자의 카테고리 조회 성공",
      data: categories,
    };
    return JSON.stringify(result);
  }
}
