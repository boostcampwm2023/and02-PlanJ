import { Controller, Post, Get, Patch, Delete, Query, Body, UseGuards, Logger } from "@nestjs/common";
import { AddScheduleDto } from "../schedule/dto/add-schedule.dto";
import { ScheduleApiService } from "./schedule-api.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";
import { DeleteScheduleDto } from "src/schedule/dto/delete-schedule.dto";
import { Token } from "../utils/token.decorator";
import { AuthGuard } from "../guard/auth.guard";
import { AddRetrospectiveMemoDto } from "./dto/add-retrospective-memo.dto";
import {
  ApiBadRequestResponse,
  ApiBearerAuth,
  ApiCreatedResponse,
  ApiOkResponse,
  ApiOperation,
  ApiTags,
  ApiUnauthorizedResponse,
} from "@nestjs/swagger";

@ApiTags("일정")
@Controller("/api/schedule")
@ApiBearerAuth("access-token")
@ApiUnauthorizedResponse({
  description: "유효한 토큰이 아닐 때",
  schema: {
    example: {
      message: "유효하지 않은 사용자입니다.",
      error: "Unauthorized",
      statusCode: 401,
    },
  },
})
@UseGuards(AuthGuard)
export class ScheduleApiController {
  private readonly logger = new Logger(ScheduleApiController.name);
  constructor(private scheduleApiService: ScheduleApiService) {}

  @ApiOperation({ summary: "날짜별 일정 조회" })
  @ApiOkResponse({
    description: "하루 일정 조회 성공",
    schema: {
      example: {
        message: "하루 일정 조회 성공",
        data: [
          {
            scheduleUuid: "01HGCZNYGBNRA1CZ2Z3ZWP94D7",
            title: "초대 일정",
            startAt: "2023-12-04T10:00:00",
            endAt: "2023-12-04T11:00:00",
            finished: true,
            failed: false,
            repeated: false,
            hasRetrospectiveMemo: false,
            shared: true,
            participantCount: 3,
            participantSuccessCount: 2,
          },
        ],
      },
    },
  })
  @Get("/daily")
  async getDailySchedule(@Token() token: string, @Query("date") date: Date): Promise<JSON> {
    const result = await this.scheduleApiService.getDailySchedule(token, date);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "주간 일정 조회" })
  @ApiOkResponse({
    description: "주간 일정 조회 성공",
    schema: {
      example: {
        message: "주간 일정 조회 성공",
        data: [
          {
            scheduleUuid: "01HGCZNYGBNRA1CZ2Z3ZWP94D7",
            title: "초대 일정",
            startAt: "2023-12-04T10:00:00",
            endAt: "2023-12-04T11:00:00",
            finished: true,
            failed: false,
            repeated: false,
            shared: true,
            participantCount: 3,
            participantSuccessCount: 2,
          },
        ],
      },
    },
  })
  @Get("/weekly")
  async getWeeklySchedule(@Token() token: string, @Query("date") date: Date) {
    const result = await this.scheduleApiService.getWeeklySchedule(token, date);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 검색", description: "빈 칸으로 검색 시 전체 조회" })
  @ApiOkResponse({
    description: "일정 검색 성공",
    schema: {
      example: {
        message: "일정 검색 성공",
        data: [
          {
            scheduleUuid: "01HGCZNYGBNRA1CZ2Z3ZWP94D7",
            title: "초대 일정",
            startAt: "2023-12-04T10:00:00",
            endAt: "2023-12-04T11:00:00",
            finished: true,
            failed: false,
            repeated: false,
            shared: true,
            participantCount: 3,
            participantSuccessCount: 2,
          },
        ],
      },
    },
  })
  @Get("/search")
  async searchByKeyword(@Token() token: string, @Query("keyword") keyword: string): Promise<JSON> {
    this.logger.log("Get /api/schedule/search");
    this.logger.verbose("Keyword: " + keyword);
    const result = await this.scheduleApiService.searchByKeyword(token, keyword);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 추가", description: "categoryUuid=default 는 미분류 카테고리에 추가" })
  @ApiCreatedResponse({
    description: "일정 추가 성공",
    schema: {
      example: {
        message: "일정 추가 성공",
        data: {
          scheduleUuid: "01HFQP6AQK85RETPHVEK09M2AC",
        },
      },
    },
  })
  @Post("/add")
  async addSchedule(@Token() token: string, @Body() dto: AddScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.addSchedule(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 업데이트", description: "categoryUuid=default 는 미분류 카테고리" })
  @ApiOkResponse({
    description: "일정 수정 성공",
    schema: {
      example: {
        message: "일정 수정 성공",
      },
    },
  })
  @Patch("/update")
  async updateSchedule(@Token() token: string, @Body() dto: UpdateScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.updateSchedule(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 삭제" })
  @ApiOkResponse({
    description: "일정 삭제 성공",
    schema: {
      example: {
        message: "일정 삭제 성공",
      },
    },
  })
  @Delete("/delete")
  async deleteSchedule(@Token() token: string, @Body() dto: DeleteScheduleDto) {
    const result = await this.scheduleApiService.deleteSchedule(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 라디오 버튼 체크" })
  @Get("/check")
  async checkedSchedule(@Token() token: string, @Query("scheduleUuid") scheduleUuid: string): Promise<JSON> {
    const result = await this.scheduleApiService.checkedSchedule(scheduleUuid);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 상세 조회" })
  @ApiOkResponse({
    description: "일정 상세 조회 성공",
    schema: {
      example: {
        message: "일정 상세 조회 성공",
        data: {
          categoryName: "테스트1",
          scheduleUuid: "01HGJGT1DXQDR0P6KB3P1TEEAC",
          title: "조회 테스트",
          description: "조회 테스트용 스케줄 입니다.",
          startAt: null,
          endAt: "2023-12-02T22:00:00",
          startLocation: {
            placeName: "서대문문역",
            placeAddress: "서울시 서대문문구 어쩌구",
            latitude: "37.571965",
            longitude: "126.977210",
          },
          endLocation: {
            placeName: "교대역역",
            placeAddress: "서울시 서초구 어쩌구",
            latitude: "37.492172",
            longitude: "127.014095",
          },
          repetition: {
            cycleType: "WEEKLY",
            cycleCount: 1,
          },
          participants: [
            {
              nickname: "young",
              profileUrl: null,
              finished: false,
              currentUser: true,
              author: true,
            },
            {
              nickname: "young123",
              profileUrl: null,
              finished: false,
              currentUser: false,
              author: false,
            },
          ],
          alarm: {
            alarmType: "END",
            alarmTime: 20,
            firstScheduleUuid: "uuid",
          },
        },
      },
    },
  })
  @Get()
  async getScheduleInfo(@Token() token: string, @Query("scheduleUuid") scheduleUuid: string): Promise<JSON> {
    this.logger.log("Get /api/schedule");
    this.logger.verbose("Schedule uuid: " + scheduleUuid);
    const result = await this.scheduleApiService.getSchedule(token, scheduleUuid);
    this.logger.verbose("Result: " + result);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "실패 일정 회고 메모 추가" })
  @ApiCreatedResponse({
    description: "회고 메모 추가 성공",
    schema: {
      example: {
        message: "회고 메모 추가 완료",
      },
    },
  })
  @ApiBadRequestResponse({
    description: "해당 일정이 존재하지 않을 때",
    schema: {
      example: {
        message: "해당 일정이 없습니다.",
        error: "Bad Request",
        statusCode: 400,
      },
    },
  })
  @ApiBadRequestResponse({
    description: "해당 일정이 실패한 일정이 아닐 때",
    schema: {
      example: {
        message: "실패한 일정이 아닙니다.",
        error: "Bad Request",
        statusCode: 400,
      },
    },
  })
  @Post("/memo")
  async addRetrospectiveMemo(@Token() token: string, @Body() dto: AddRetrospectiveMemoDto): Promise<JSON> {
    this.logger.log("Post /api/schedule/memo");
    this.logger.verbose("Data: " + JSON.stringify(dto, null, 2));
    const result = await this.scheduleApiService.addRetrospectiveMemo(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "실패 회고 메모 목록 조회" })
  @ApiOkResponse({
    description: "실패 회고 메모 목록 조회 성공",
    schema: {
      example: {
        message: "실패 메모 조회 성공",
        data: [
          {
            title: "공유",
            startAt: null,
            endAt: "2023-12-08T01:00:00",
            retrospectiveMemo: "삭제 안된 거",
          },
          {
            title: "삭제",
            startAt: null,
            endAt: "2023-12-05T23:00:00",
            retrospectiveMemo: "삭제된 거",
          },
        ],
      },
    },
  })
  @Get("/memo")
  async getRetrospectiveMemo(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /api/schedule/memo");
    this.logger.verbose("Token" + token);
    const result = await this.scheduleApiService.getRetrospectiveMemo(token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "알람 일정 조회" })
  @ApiOkResponse({
    description: "알람 일정 조회 성공",
    schema: {
      example: {
        message: "알람 일정 조회 성공",
        data: [
          {
            title: "알림 테스트",
            endAt: "2023-12-09T01:00:00",
            scheduleUuid: "01HH6QPP6P37Y87JNZT62J0257",
            alarmTime: 10,
            alarmType: "END",
            estimatedTime: 0,
          },
        ],
      },
    },
  })
  @Get("/alarm")
  async getScheduleHasAlarm(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /api/schedule/alarm");
    this.logger.verbose("Token: " + token);
    const result = await this.scheduleApiService.getScheduleHasAlarm(token);
    return JSON.parse(result);
  }
}
