import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "src/schedule/dto/add-schedule.dto";
import { ScheduleMetaService } from "src/schedule/schedule-meta.service";
import { ScheduleService } from "src/schedule/schedule.service";
import { UserService } from "src/user/user.service";
import { CategoryService } from "src/category/category.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";

@Injectable()
export class ScheduleApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private scheduleMetaService: ScheduleMetaService,
    private scheduleService: ScheduleService,
  ) {}

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    const user = await this.userService.getUserEntity(dto.userUuid);
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const schdeuleMetadata = await this.scheduleMetaService.addScheduleMetadata(dto, user, category);
    return await this.scheduleService.addSchedule(dto, schdeuleMetadata);
  }

  async updateSchedule(dto: UpdateScheduleDto) {
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const metadataId = await this.scheduleService.getMetadataIdByScheduleUuid(dto.scheduleUuid);
    await this.scheduleMetaService.updateScheduleMetadata(dto, category, metadataId);
    return await this.scheduleService.updateSchedule(dto);
  }

  async getDailySchedule(userUuid: string, date: Date): Promise<string> {
    const user = await this.userService.getUserEntity(userUuid);
    return await this.scheduleMetaService.getAllScheduleByDate(user, date);
  }

  async getWeeklySchedule(userUuid: string, date: Date): Promise<string> {
    const user = await this.userService.getUserEntity(userUuid);
    return await this.scheduleMetaService.getAllScheduleByWeek(user, date);
  }
}