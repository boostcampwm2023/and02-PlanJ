import { ScheduleLocationEntity } from "./../schedule/entity/schedule-location.entity";
import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "src/schedule/dto/add-schedule.dto";
import { ScheduleMetaService } from "src/schedule/schedule-meta.service";
import { ScheduleService } from "src/schedule/schedule.service";
import { UserService } from "src/user/user.service";
import { CategoryService } from "src/category/category.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";
import { DeleteScheduleDto } from "src/schedule/dto/delete-schedule.dto";
import { ScheduleLocationService } from "src/schedule/schedule-location.service";

@Injectable()
export class ScheduleApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private scheduleMetaService: ScheduleMetaService,
    private scheduleService: ScheduleService,
    private scheduleLocationService: ScheduleLocationService,
  ) {}

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    const user = await this.userService.getUserEntity(dto.userUuid);
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const schdeuleMetadata = await this.scheduleMetaService.addScheduleMetadata(dto, user, category);
    return await this.scheduleService.addSchedule(dto, schdeuleMetadata);
  }

  async updateSchedule(dto: UpdateScheduleDto) {
    const user = await this.userService.getUserEntity(dto.userUuid); // checkUser로 이름 변경
    // 시간(schedule) 위치(location), 반복(repitition) 이름, 설명
    // 일단 meta_data -> 비교 각각 -> 변경사항 없으면 시간, 위치, 반복 -> 변경로직 불필요
    // 이름, 설명은 그냥 삽입
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid); // 음
    const metadataId = await this.scheduleService.getMetadataIdByScheduleUuid(dto.scheduleUuid);
    const scheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(dto, category, metadataId);
    await this.scheduleLocationService.updateLocation(dto, scheduleMeta);
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

  async deleteSchedule(dto: DeleteScheduleDto): Promise<string> {
    const user = await this.userService.getUserEntity(dto.userUuid);
    const metadataId = await this.scheduleService.deleteSchedule(dto);
    return await this.scheduleMetaService.deleteScheduleMeta(metadataId);
  }
}
