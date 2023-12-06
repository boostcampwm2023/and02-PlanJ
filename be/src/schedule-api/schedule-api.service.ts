import { ForbiddenException, Injectable, Logger } from "@nestjs/common";
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
import { ScheduleAlarmService } from "../schedule/schedule-alarm.service";
import { ScheduleResponse } from "src/schedule/dto/schedule.response";
import { ParticipantEntity } from "../schedule/entity/participant.entity";
import { ParticipantResponse } from "./dto/participant.response";
import { UserEntity } from "../user/entity/user.entity";
import { ScheduleDetailResponse } from "./dto/schedule-detail.response";
import { LocationResponse } from "./dto/location.response";
import { ScheduleLocationEntity } from "../schedule/entity/schedule-location.entity";
import { AddRetrospectiveMemoDto } from "./dto/add-retrospective-memo.dto";
import { ScheduleEntity } from "../schedule/entity/schedule.entity";
import { RepetitionDto } from "src/schedule/dto/repetition.dto";
import { InviteStatus } from "src/utils/domain/invite-status.enum";

@Injectable()
export class ScheduleApiService {
  private readonly logger = new Logger(ScheduleApiService.name);

  constructor(
    private userService: UserService,
    private categoryService: CategoryService,
    private scheduleMetaService: ScheduleMetaService,
    private scheduleService: ScheduleService,
    private scheduleLocationService: ScheduleLocationService,
    private authService: AuthService,
    private repetitionService: RepetitionService,
    private participateService: ParticipateService,
    private scheduleAlarmService: ScheduleAlarmService,
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

  async getSchedule(token: string, scheduleUuid: string) {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    const scheduleEntity = await this.scheduleService.getScheduleEntityByScheduleUuid(scheduleUuid);
    const scheduleMetadata = await this.scheduleMetaService.getScheduleMetadataById(scheduleEntity.metadataId);

    if (user.userId !== scheduleMetadata.userId) {
      throw new ForbiddenException("해당 사용자에게 권한이 없습니다.");
    }

    const [scheduleLocationEntity, scheduleAlarmEntity, categoryEntity, repetitionEntity, participants] =
      await Promise.all([
        this.scheduleLocationService.getLocationByScheduleMetadataId(scheduleMetadata.metadataId),
        this.scheduleAlarmService.getAlarmByMetadataId(scheduleMetadata.metadataId),
        this.categoryService.getCategoryEntityByCategoryId(scheduleMetadata.categoryId),
        this.repetitionService.getRepetitionByMetadataId(scheduleMetadata.metadataId),
        this.participateService.getParticipantGroup(scheduleMetadata.metadataId),
      ]);

    const participantsInfo = await this.getParticipantSuccess(participants, scheduleEntity.endAt, user);

    if (participantsInfo.length === 0) {
      const response: ParticipantResponse = {
        nickname: user.nickname,
        email: user.email,
        author: true,
        profileUrl: user.profileUrl,
        finished: scheduleEntity.finished,
        currentUser: true,
      };

      participantsInfo.push(response);
    }

    const scheduleDetailResponse: ScheduleDetailResponse = {
      categoryName: !!categoryEntity ? categoryEntity.categoryName : "미분류",
      scheduleUuid: scheduleEntity.scheduleUuid,
      title: scheduleMetadata.title,
      description: scheduleMetadata.description,
      startAt: scheduleEntity.startAt,
      endAt: scheduleEntity.endAt,
      startLocation: scheduleLocationEntity ? this.getStartLocation(scheduleLocationEntity) : null,
      endLocation: scheduleLocationEntity ? this.getEndLocation(scheduleLocationEntity) : null,
      repetition: repetitionEntity
        ? {
            cycleType: repetitionEntity.cycleType,
            cycleCount: repetitionEntity.cycleCount,
          }
        : null,
      participants: participantsInfo,
      alarm: scheduleAlarmEntity
        ? {
            alarmType: scheduleAlarmEntity.alarmType,
            alarmTime: scheduleAlarmEntity.alarmTime,
          }
        : null,
    };

    const body: HttpResponse = {
      message: "일정 상세 조회 성공",
      data: scheduleDetailResponse,
    };
    return JSON.stringify(body);
  }

  async updateSchedule(token: string, dto: UpdateScheduleDto) {
    dto.userUuid = this.authService.verify(token);
    const category = await this.categoryService.getCategoryEntity(dto.categoryUuid);
    const metadataId = await this.scheduleService.getMetadataIdByScheduleUuid(dto.scheduleUuid);
    const scheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(dto, category, metadataId);
    await this.scheduleLocationService.updateLocation(dto, scheduleMeta);
    const repetitionChanged = await this.repetitionService.updateRepetition(dto.repetition, scheduleMeta);
    // 따로 해야할 듯
    await this.scheduleService.updateSchedule(dto, scheduleMeta, repetitionChanged);
    await this.scheduleAlarmService.addScheduleAlarm(dto, scheduleMeta);

    const authorGroup = await this.participateService.getAuthorGroup(metadataId);

    const groupUserEntities: UserEntity[] = [];
    for (const entity of authorGroup) {
      const user = await this.userService.getUserEntityById(entity.participant.userId);
      groupUserEntities.push(user);
    }

    const invitedUserEntities: UserEntity[] = [];
    for (const participantEmail of dto.participants) {
      const entity = await this.userService.getUserEntityByEmail(participantEmail);
      invitedUserEntities.push(entity);
    }

    const invitedStatus = await this.participateService.checkInvitedStatus(
      scheduleMeta,
      groupUserEntities,
      invitedUserEntities,
    );

    if (!!dto.participants) {
      for (const [email, invitedStatusEnum] of invitedStatus) {
        await this.inviteSchedule(email, invitedStatusEnum, dto.scheduleUuid, dto.repetition);
      }

      await this.scheduleMetaService.updateSharedStatus(metadataId);
    }

    const body: HttpResponse = {
      message: "일정 수정 성공",
    };
    return JSON.stringify(body);
  }

  async getDailySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    const [scheduleResponses, updatedSchedules]: [ScheduleResponse[], ScheduleEntity[]] =
      await this.scheduleMetaService.getAllScheduleByDate(user, date);

    await Promise.all([
      this.getParticipantInformation(scheduleResponses),
      this.scheduleService.updateScheduleEntities(updatedSchedules),
    ]);

    const body: HttpResponse = {
      message: "하루 일정 조회 성공",
      data: scheduleResponses,
    };
    return JSON.stringify(body);
  }

  async getWeeklySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    const [scheduleResponses, updatedSchedules]: [ScheduleResponse[], ScheduleEntity[]] =
      await this.scheduleMetaService.getAllScheduleByWeek(user, date);

    await Promise.all([
      this.getParticipantInformation(scheduleResponses),
      this.scheduleService.updateScheduleEntities(updatedSchedules),
    ]);

    const body: HttpResponse = {
      message: "주간 일정 조회 성공",
      data: scheduleResponses,
    };
    return JSON.stringify(body);
  }

  private async getParticipantInformation(scheduleResponses: ScheduleResponse[]) {
    for (const scheduleResponse of scheduleResponses) {
      const metadataId = await this.scheduleService.getMetadataIdByScheduleUuid(scheduleResponse.scheduleUuid);
      const group = await this.participateService.getParticipantGroup(metadataId);

      if (group === null) {
        continue;
      }

      const endAt = scheduleResponse.endAt;
      scheduleResponse.participantCount = group.length;

      for (const participant of group) {
        const check = await this.scheduleService.checkScheduleSuccessByMetadataIdAndEndAt(
          participant.participantId,
          endAt,
        );
        if (check) {
          scheduleResponse.participantSuccessCount += 1;
        }
      }
    }
  }

  private async getParticipantSuccess(participantEntities: ParticipantEntity[], endAt: string, user: UserEntity) {
    const participants = participantEntities ?? [];
    return await Promise.all(
      participants.map(async (participant) => {
        const scheduleMeta = await this.scheduleMetaService.getScheduleMetadataById(participant.participantId);
        const [userEntity, success] = await Promise.all([
          this.userService.getUserEntityById(scheduleMeta.userId),
          this.scheduleService.checkScheduleSuccessByMetadataIdAndEndAt(scheduleMeta.metadataId, endAt),
        ]);

        const result: ParticipantResponse = {
          nickname: userEntity.nickname,
          email: userEntity.email,
          author: participant.authorId === participant.participantId,
          profileUrl: userEntity.profileUrl,
          finished: success,
          currentUser: user.userId === scheduleMeta.userId,
        };
        return result;
      }),
    );
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

  async checkedSchedule(scheduleUuid: string): Promise<string> {
    const data = await this.scheduleService.checkedSchedule(scheduleUuid);

    const body: HttpResponse = {
      message: "일정 체크 처리 완료",
      data: {
        failed: data.failed,
        hasRetrospectiveMemo: data.hasRetrospectiveMemo,
      },
    };
    return JSON.stringify(body);
  }

  async inviteSchedule(
    invitedUserEmail: string,
    invitedStatus: InviteStatus,
    authorScheduleUuid: string,
    repetition: RepetitionDto,
  ) {
    // invitedStatusCode 0: not added (new) 1: already added(changed) 2: deleted

    const authorMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(authorScheduleUuid);
    const authorSchedule = await this.scheduleService.getScheduleEntityByScheduleUuid(authorScheduleUuid);
    const authorScheduleMetadata = authorSchedule.parent;
    const authorScheduleLocation = await this.scheduleLocationService.getLocationByScheduleMetadataId(authorMetadataId);

    const invitedUser = await this.userService.getUserEntityByEmail(invitedUserEmail);

    let invitedScheduleUuid: string;
    let invitedMetadataId: number;

    if (invitedStatus === InviteStatus.DELETED) {
      const unInvitedMetadataId = await this.participateService.getInvitedMetadataId(
        authorMetadataId,
        invitedUser.userId,
      );
      const unInvitedScheduleUuid = await this.scheduleService.getFirstScheduleUuidByMetadataId(unInvitedMetadataId);
      const unInvitedMetadata = await this.scheduleMetaService.getScheduleMetadataById(unInvitedMetadataId);
      const unInvitedUserEntity = await this.userService.getUserEntityById(unInvitedMetadata.userId);

      const deleteScheduleDto: DeleteScheduleDto = {
        userUuid: unInvitedUserEntity.userUuid,
        scheduleUuid: unInvitedScheduleUuid,
      };

      const metadataId = await this.scheduleService.deleteSchedule(deleteScheduleDto);
      await this.scheduleMetaService.deleteScheduleMeta(metadataId);
      await this.participateService.unInviteSchedule(authorScheduleMetadata.metadataId, unInvitedMetadataId);
      return;
    }

    if (invitedStatus === InviteStatus.NEW) {
      const addScheduleDto: AddScheduleDto = {
        userUuid: invitedUser.userUuid,
        categoryUuid: "default",
        title: authorScheduleMetadata.title,
        endAt: authorSchedule.endAt,
      };

      const invitedScheduleMetadata = await this.scheduleMetaService.addScheduleMetadata(addScheduleDto, invitedUser);
      invitedScheduleUuid = await this.scheduleService.addSchedule(addScheduleDto, invitedScheduleMetadata);
      invitedMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(invitedScheduleUuid);
      await this.scheduleMetaService.updateSharedStatus(invitedMetadataId);
    }
    if (invitedStatus === InviteStatus.CHANGED) {
      invitedMetadataId = await this.participateService.getInvitedMetadataId(authorMetadataId, invitedUser.userId);
      invitedScheduleUuid = await this.scheduleService.getFirstScheduleUuidByMetadataId(invitedMetadataId);
    }

    const startLocation: ScheduleLocationDto = !!authorScheduleLocation
      ? {
          placeName: authorScheduleLocation.startPlaceName,
          placeAddress: authorScheduleLocation.startPlaceAddress,
          latitude: authorScheduleLocation.startLatitude,
          longitude: authorScheduleLocation.startLongitude,
        }
      : null;

    const endLocation: ScheduleLocationDto = !!authorScheduleLocation
      ? {
          placeName: authorScheduleLocation.endPlaceName ?? null,
          placeAddress: authorScheduleLocation.endPlaceAddress ?? null,
          latitude: authorScheduleLocation.endLatitude ?? null,
          longitude: authorScheduleLocation.endLongitude ?? null,
        }
      : null;

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

    const invitedScheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(
      updateScheduleDto,
      null,
      invitedMetadataId,
    );

    await this.scheduleLocationService.updateLocation(updateScheduleDto, invitedScheduleMeta);
    const repetitionChanged = await this.repetitionService.updateRepetition(repetition, invitedScheduleMeta);
    await this.scheduleService.updateSchedule(updateScheduleDto, invitedScheduleMeta, repetitionChanged);

    if (invitedStatus === InviteStatus.NEW) {
      await this.participateService.inviteSchedule(authorScheduleMetadata, invitedMetadataId);
    }
  }

  private getStartLocation(scheduleLocationEntity: ScheduleLocationEntity) {
    if (!scheduleLocationEntity.startLatitude) {
      return null;
    }

    const result: LocationResponse = {
      placeName: scheduleLocationEntity.startPlaceName,
      placeAddress: scheduleLocationEntity.startPlaceAddress,
      latitude: scheduleLocationEntity.startLatitude,
      longitude: scheduleLocationEntity.startLongitude,
    };
    return result;
  }

  private getEndLocation(scheduleLocationEntity: ScheduleLocationEntity) {
    const result: LocationResponse = {
      placeName: scheduleLocationEntity.endPlaceName,
      placeAddress: scheduleLocationEntity.endPlaceAddress,
      latitude: scheduleLocationEntity.endLatitude,
      longitude: scheduleLocationEntity.endLongitude,
    };
    return result;
  }

  async addRetrospectiveMemo(token: string, dto: AddRetrospectiveMemoDto) {
    await this.scheduleService.addRetrospectiveMemo(dto);

    const result: HttpResponse = {
      message: "회고 메모 추가 완료",
    };
    this.logger.verbose(result);
    return JSON.stringify(result);
  }

  async searchByKeyword(token: string, keyword: string) {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);
    const [scheduleResponses, updatedSchedules] = await this.scheduleMetaService.getAllScheduleByKeyword(
      keyword,
      userEntity.userId,
    );

    await Promise.all([
      this.getParticipantInformation(scheduleResponses),
      this.scheduleService.updateScheduleEntities(updatedSchedules),
    ]);

    scheduleResponses.sort((a, b) => a.endAt.localeCompare(b.endAt));

    const body: HttpResponse = {
      message: "일정 검색 성공",
      data: scheduleResponses,
    };
    return JSON.stringify(body);
  }

  async getRetrospectiveMemo(token: string): Promise<string> {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);
    const memoResponses = await this.scheduleMetaService.getRetrospectiveMemoByUserId(userEntity.userId);

    const body: HttpResponse = {
      message: "실패 메모 조회 성공",
      data: memoResponses,
    };
    return JSON.stringify(body);
  }
}
