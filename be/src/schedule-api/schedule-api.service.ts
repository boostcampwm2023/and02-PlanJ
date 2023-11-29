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
import { ScheduleLocationDto } from "src/schedule/dto/schedule-location.dto";
import { HttpResponse } from "src/utils/http.response";

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
    const scheduleUuid = await this.scheduleService.addSchedule(dto, scheduleMetadata);

    const body: HttpResponse = {
      message: "일정 추가 성공",
      data: {
        scheduleUuid: scheduleUuid,
      },
    };
    return JSON.stringify(body);
  }

  async updateSchedule(token: string, dto: UpdateScheduleDto) {
    dto.userUuid = this.authService.verify(token);
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const metadataId = await this.scheduleService.getMetadataIdByScheduleUuid(dto.scheduleUuid);
    const scheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(dto, category, metadataId);
    await this.scheduleLocationService.updateLocation(dto, scheduleMeta);
    await this.repetitionService.updateRepetition(dto, scheduleMeta);
    await this.scheduleService.updateSchedule(dto, scheduleMeta);
    for (const email of dto.participants) {
      await this.inviteSchedule(dto.scheduleUuid, email);
    }

    const body: HttpResponse = {
      message: "일정 수정 성공",
    };
    return JSON.stringify(body);
  }

  async getDailySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    const schedules = await this.scheduleMetaService.getAllScheduleByDate(user, date);

    const body: HttpResponse = {
      message: "하루 일정 조회 성공",
      data: schedules,
    };
    return JSON.stringify(body);
  }

  async getWeeklySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    const schedules = await this.scheduleMetaService.getAllScheduleByWeek(user, date);

    const body: HttpResponse = {
      message: "주간 일정 조회 성공",
      data: schedules,
    };
    return JSON.stringify(body);
  }

  async deleteSchedule(token: string, dto: DeleteScheduleDto): Promise<string> {
    dto.userUuid = this.authService.verify(token);
    const metadataId = await this.scheduleService.deleteSchedule(dto);
    await this.scheduleMetaService.deleteScheduleMeta(metadataId);

    const body: HttpResponse = {
      message: "일정 삭제 성공",
    };
    return JSON.stringify(body);
  }

  async inviteSchedule(authorScheduleUuid: string, invitedUserEmail: string) {
    const authorMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(authorScheduleUuid);
    const authorSchedule = await this.scheduleService.getScheduleEntityByScheduleUuid(authorScheduleUuid);
    const authorScheduleMetadata = authorSchedule.parent;
    const authorScheduleLocation = await this.scheduleLocationService.getLocationByScheduleMetadataId(authorMetadataId);

    const invitedUser = await this.userService.getUserEntityByEmail(invitedUserEmail);

    const addScheduleDto: AddScheduleDto = {
      userUuid: invitedUser.userUuid,
      categoryUuid: "default",
      title: authorScheduleMetadata.title,
      endAt: authorSchedule.endAt,
    };

    const invitedScheduleMetadata = await this.scheduleMetaService.addScheduleMetadata(addScheduleDto, invitedUser);
    const invitedScheduleUuid = await this.scheduleService.addSchedule(addScheduleDto, invitedScheduleMetadata);
    const invitedMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(invitedScheduleUuid);

    const startLocation: ScheduleLocationDto = {
      placeName: authorScheduleLocation.startPlaceName,
      placeAddress: authorScheduleLocation.startPlaceAddress,
      latitude: authorScheduleLocation.startLatitude,
      longitude: authorScheduleLocation.startLongitude,
    };

    const endLocation: ScheduleLocationDto = {
      placeName: authorScheduleLocation.endPlaceName,
      placeAddress: authorScheduleLocation.endPlaceAddress,
      latitude: authorScheduleLocation.endLatitude,
      longitude: authorScheduleLocation.endLongitude,
    };

    const updateScheduleDto: UpdateScheduleDto = {
      categoryUuid: "default",
      scheduleUuid: invitedScheduleUuid,
      title: authorScheduleMetadata.title,
      description: authorScheduleMetadata.description,
      startAt: authorSchedule.startAt,
      endAt: authorSchedule.endAt,
      startLocation,
      endLocation,
    };

    const scheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(
      updateScheduleDto,
      null,
      invitedMetadataId,
    );
    await this.scheduleLocationService.updateLocation(updateScheduleDto, scheduleMeta);
    await this.scheduleService.updateSchedule(updateScheduleDto, scheduleMeta);
    return await this.participateService.inviteSchedule(authorScheduleMetadata, invitedMetadataId);
  }
}
