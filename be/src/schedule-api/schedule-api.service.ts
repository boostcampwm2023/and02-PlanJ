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
import { CategoryEntity } from "../category/entity/category.entity";
import { PushService } from "../push/push.service";
import { ScheduleAlarmEntity } from "../schedule/entity/schedule-alarm.entity";
import { RepetitionEntity } from "../schedule/entity/repetition.entity";

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
    private pushService: PushService,
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

    const [scheduleLocationEntity, scheduleAlarmEntity, categoryEntity, repetitionEntity, participants]: [
      ScheduleLocationEntity,
      ScheduleAlarmEntity,
      CategoryEntity,
      RepetitionEntity,
      ParticipantEntity[],
    ] = await Promise.all([
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
            estimatedTime: scheduleAlarmEntity.estimatedTime,
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

    const [category, metadataId]: [CategoryEntity, number] = await Promise.all([
      this.categoryService.getCategoryEntity(dto.categoryUuid),
      this.scheduleService.getMetadataIdByScheduleUuid(dto.scheduleUuid),
    ]);

    const author = await this.participateService.checkIsAuthor(metadataId);
    const scheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(dto, category, metadataId, author);
    await Promise.all([
      this.scheduleAlarmService.updateScheduleAlarm(dto, scheduleMeta),
      this.scheduleLocationService.updateLocation(dto, scheduleMeta, author),
    ]);

    if (author) {
      const repetitionChanged = await this.repetitionService.updateRepetition(dto.repetition, scheduleMeta);
      await this.scheduleService.updateSchedule(dto, scheduleMeta, repetitionChanged);
      const authorGroup = await this.participateService.getAuthorGroup(metadataId);

      const groupUserEntities: UserEntity[] = await Promise.all(
        authorGroup.map(async (entity) => {
          return this.userService.getUserEntityById(entity.participant.userId);
        }),
      );

      const invitedUserEntities: UserEntity[] = await Promise.all(
        dto.participants.map(async (email) => {
          return this.userService.getUserEntityByEmail(email);
        }),
      );

      const invitedStatus = await this.participateService.checkInvitedStatus(
        scheduleMeta,
        groupUserEntities,
        invitedUserEntities,
      );

      for (const [email, invitedStatusEnum] of invitedStatus) {
        await this.inviteSchedule(email, invitedStatusEnum, dto.scheduleUuid, dto.repetition);
      }

      const isAllUnInvited = dto.participants.length === 0;
      if (isAllUnInvited) {
        await this.participateService.deleteAuthor(metadataId);
      }
    }

    const body: HttpResponse = {
      message: "일정 수정 성공",
    };
    return JSON.stringify(body);
  }

  async getDailySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    const [scheduleResponses, updatedSchedules, repeatedSchedules]: [
      ScheduleResponse[],
      ScheduleEntity[],
      [number, ScheduleEntity][],
    ] = await this.scheduleMetaService.getAllScheduleByDate(user, date);

    await Promise.all([
      this.getParticipantInformation(scheduleResponses),
      this.scheduleService.updateScheduleEntities(updatedSchedules),
    ]);
    this.updateRepeatedSchedules(repeatedSchedules);

    const body: HttpResponse = {
      message: "하루 일정 조회 성공",
      data: scheduleResponses,
    };
    return JSON.stringify(body);
  }

  async getWeeklySchedule(token: string, date: Date): Promise<string> {
    const userUuid = this.authService.verify(token);
    const user = await this.userService.getUserEntity(userUuid);
    const [scheduleResponses, updatedSchedules, repeatedSchedules]: [
      ScheduleResponse[],
      ScheduleEntity[],
      [number, ScheduleEntity][],
    ] = await this.scheduleMetaService.getAllScheduleByWeek(user, date);

    await Promise.all([
      this.getParticipantInformation(scheduleResponses),
      this.scheduleService.updateScheduleEntities(updatedSchedules),
    ]);
    this.updateRepeatedSchedules(repeatedSchedules);

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
    const metadata = await this.scheduleMetaService.getScheduleMetadataById(metadataId);
    await this.scheduleMetaService.deleteScheduleMeta(metadataId);

    if (metadata.shared) {
      const message = `공유된 ${metadata.title} 일정이 삭제되었습니다.`;
      const participants = await this.participateService.getParticipantGroup(metadataId);
      const participantList = participants.filter((participant) => participant.participantId !== participant.authorId);
      const [metadataLists, ,] = await Promise.all([
        Promise.all(
          participantList.map(async (participant) => {
            return this.scheduleMetaService.getScheduleMetadataById(participant.participantId);
          }),
        ),
        this.participateService.deleteGroup(metadataId),
        participants.map(async (participant) => {
          this.scheduleMetaService.deleteScheduleMeta(participant.participantId);
        }),
      ]);

      const users = await Promise.all(
        metadataLists.map(async (meta) => {
          return this.userService.getUserEntityById(meta.userId);
        }),
      );

      users.forEach((user) => {
        if (!!user.deviceToken) {
          this.pushService.sendPush(user.deviceToken, message);
        }
      });
    }

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

  private async inviteSchedule(
    invitedUserEmail: string,
    invitedStatus: InviteStatus,
    authorScheduleUuid: string,
    repetition: RepetitionDto,
  ) {
    const authorMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(authorScheduleUuid);
    const authorSchedule = await this.scheduleService.getScheduleEntityByScheduleUuid(authorScheduleUuid);
    const authorScheduleMetadata = authorSchedule.parent;
    const authorScheduleLocation = await this.scheduleLocationService.getLocationByScheduleMetadataId(authorMetadataId);
    let message: string;

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
      message = `새로운 ${authorScheduleMetadata.title} 일정이 공유되었습니다.`;
      if (!!invitedUser.deviceToken) {
        this.pushService.sendPush(invitedUser.deviceToken, message);
      }
      const addScheduleDto: AddScheduleDto = {
        userUuid: invitedUser.userUuid,
        categoryUuid: "default",
        title: authorScheduleMetadata.title,
        endAt: authorSchedule.endAt,
      };

      const invitedScheduleMetadata = await this.scheduleMetaService.addScheduleMetadata(addScheduleDto, invitedUser);
      invitedScheduleUuid = await this.scheduleService.addSchedule(addScheduleDto, invitedScheduleMetadata);
      invitedMetadataId = await this.scheduleService.getMetadataIdByScheduleUuid(invitedScheduleUuid);
    } else if (invitedStatus === InviteStatus.CHANGED) {
      message = `공유된 ${authorScheduleMetadata.title} 일정이 변경되었습니다.`;
      invitedMetadataId = await this.participateService.getInvitedMetadataId(authorMetadataId, invitedUser.userId);
      invitedScheduleUuid = await this.scheduleService.getFirstScheduleUuidByMetadataId(invitedMetadataId);
    }

    const startLocation: ScheduleLocationDto = null;
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
      repetition,
      participants: ["invited"],
      startLocation,
      endLocation,
    };

    const invitedScheduleMeta = await this.scheduleMetaService.updateScheduleMetadata(
      updateScheduleDto,
      null,
      invitedMetadataId,
      true,
    );

    await this.scheduleLocationService.updateLocation(updateScheduleDto, invitedScheduleMeta, true);
    const repetitionChanged = await this.repetitionService.updateRepetition(repetition, invitedScheduleMeta);
    this.logger.verbose(`${invitedUserEmail} : ${repetitionChanged}`);
    await this.scheduleService.updateSchedule(updateScheduleDto, invitedScheduleMeta, repetitionChanged);

    if (invitedStatus === InviteStatus.NEW) {
      await this.participateService.inviteSchedule(authorScheduleMetadata, invitedMetadataId);
    }

    if (!!invitedUser.deviceToken) {
      this.pushService.sendPush(invitedUser.deviceToken, message);
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

  private updateRepeatedSchedules(repeatedSchedules: [number, ScheduleEntity][]) {
    repeatedSchedules.map(async ([metadataId, schedule]) => {
      const repetitionEntity = await this.repetitionService.getRepetitionByMetadataId(metadataId);
      const repetition: RepetitionDto = {
        cycleType: repetitionEntity.cycleType,
        cycleCount: repetitionEntity.cycleCount,
      };

      this.scheduleService.addRepeatedSchedule(schedule, repetition);
    });
  }

  async getScheduleHasAlarm(token: string) {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);
    const records = await this.scheduleMetaService.getScheduleHasAlarm(userEntity.userId);

    const body: HttpResponse = {
      message: "알람 일정 조회 성공",
      data: records,
    };
    return JSON.stringify(body);
  }
}
