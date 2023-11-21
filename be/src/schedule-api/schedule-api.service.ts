import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "src/schedule/dto/add-schedule.dto";
import { ScheduleMetadataService } from "src/schedule/schedule-metadata.service";
import { ScheduleService } from "src/schedule/schedule.service";
import { UserService } from "src/user/user.service";
import { CategoryService } from "src/category/category.service";

@Injectable()
export class ScheduleApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private scheduleMetadataService: ScheduleMetadataService,
    private scheduleService: ScheduleService,
  ) {}

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    const user = await this.userService.getUserEntity(dto.userUuid);
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const schdeuleMetadata = await this.scheduleMetadataService.addScheduleMetadata(dto, user, category);
    return await this.scheduleService.addSchedule(dto, schdeuleMetadata);
  }

  async getDailySchedule(userUuid: string, date: Date) {
    const user = await this.userService.getUserEntity(userUuid);
    return await this.
  }

  getWeekly() {}
}
