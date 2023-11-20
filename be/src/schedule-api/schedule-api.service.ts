import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "src/schedule/dto/add-schedule.dto";
import { GetUserEntityService } from "src/schedule/get-user-entity.service";
import { GetCategoryEntityService } from "src/schedule/get-category-entity.service";
import { AddScheduleMetadataService } from "src/schedule/add-schedule-metadata.service";
import { AddScheduleService } from "src/schedule/add-schedule.service";

@Injectable()
export class ScheduleApiService {
  constructor(
    private getUserEntityService: GetUserEntityService,
    private getCategoryEntityService: GetCategoryEntityService,
    private addScheduleMetadataService: AddScheduleMetadataService,
    private addScheduleService: AddScheduleService,
  ) {}

  async addSchedule(dto: AddScheduleDto): Promise<string> {
    const user = await this.getUserEntityService.getUserEntity(dto);
    const category = await this.getCategoryEntityService.getCategoryEntity(dto);
    const schdeuleMetadata = await this.addScheduleMetadataService.addScheduleMetadata(dto, user, category);
    return await this.addScheduleService.addSchedule(dto, schdeuleMetadata);
  }

  getDaily() {}

  getWeekly() {}
}
