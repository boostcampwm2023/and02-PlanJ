import { Injectable } from "@nestjs/common";
import { AddScheduleDto } from "src/schedule/dto/add-schedule.dto";
import { ScheduleMetaService } from "src/schedule/schedule-meta.service";
import { ScheduleService } from "src/schedule/schedule.service";
import { UserService } from "src/user/user.service";
import { CategoryService } from "src/category/category.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";
import { DeleteScheduleDto } from "src/schedule/dto/delete-schedule.dto";
import { ScheduleLocationService } from "src/schedule/schedule-location.service";
import { AuthService } from "../auth/auth.service";
import { RepetitionService } from "../schedule/repetition.service";
import { ParticipateService } from "src/schedule/participate.service";
import { InviteScheduleDto } from "src/schedule/dto/invite-schedule.dto";

@Injectable()
export class ScheduleApiService {
  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private scheduleMetaService: ScheduleMetaService,
    private scheduleService: ScheduleService,
    private scheduleLocationService: ScheduleLocationService,
    private authService: AuthService,
    private repetitionService: RepetitionService,
    private participateService: ParticipateService,
  ) {}

  async addSchedule(token: string, dto: AddScheduleDto): Promise<string> {
    dto.userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(dto.userUuid);
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const scheduleMetadata = await this.scheduleMetaService.addScheduleMetadata(dto, user, category);
    return await this.scheduleService.addSchedule(dto, scheduleMetadata);
  }

  async updateSchedule(token: string, dto: UpdateScheduleDto) {
    dto.userUuid = this.authService.verify(token);
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const metadataId = await this.scheduleService.getMetadataIdByScheduleUuid(dto.scheduleUuid);
    const scheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(dto, category, metadataId);
    await this.scheduleLocationService.updateLocation(dto, scheduleMeta);
    await this.repetitionService.updateRepetition(dto, metadataId);
    return await this.scheduleService.updateSchedule(dto);
  }

  async getDailySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    return await this.scheduleMetaService.getAllScheduleByDate(user, date);
  }

  async getWeeklySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    return await this.scheduleMetaService.getAllScheduleByWeek(user, date);
  }

  async deleteSchedule(token: string, dto: DeleteScheduleDto): Promise<string> {
    dto.userUuid = this.authService.verify(token);
    const metadataId = await this.scheduleService.deleteSchedule(dto);
    return await this.scheduleMetaService.deleteScheduleMeta(metadataId);
  }

  async inviteSchedule(token: string, dto: InviteScheduleDto) {
    const userUuid = this.authService.verify(token);
    const authorMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(dto.scheduleUuid);
    const endAt = await this.scheduleService.getEndAtByScheduleUuid(dto.scheduleUuid);

    const invitedUser = await this.userService.getUserEntityByEmail(dto.invitedUserEmail);

    const addScheduleDto: AddScheduleDto = {
      userUuid: invitedUser.userUuid,
      categoryUuid: "default",
      title: "title",
      endAt,
    };

    const invitedScheduleMetadata = await this.scheduleMetaService.addScheduleMetadata(addScheduleDto, invitedUser);
    const invitedHttpResponse = await this.scheduleService.addSchedule(addScheduleDto, invitedScheduleMetadata);
    const scheduleUuid = JSON.parse(invitedHttpResponse).data.scheduleUuid;

    const invitedMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(scheduleUuid);
    return await this.participateService.inviteSchedule(authorMetadataId, invitedMetadataId);
  }
}
