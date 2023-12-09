import { Controller, Post, Get, Patch, Delete, Query, Body, UseGuards, Logger } from "@nestjs/common";
import { AddScheduleDto } from "../schedule/dto/add-schedule.dto";
import { ScheduleApiService } from "./schedule-api.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";
import { DeleteScheduleDto } from "src/schedule/dto/delete-schedule.dto";
import { Token } from "../utils/token.decorator";
import { AuthGuard } from "../guard/auth.guard";
import { AddRetrospectiveMemoDto } from "./dto/add-retrospective-memo.dto";
import { ApiOperation, ApiTags } from "@nestjs/swagger";

@ApiTags("일정")
@Controller("/api/schedule")
@UseGuards(AuthGuard)
export class ScheduleApiController {
  private readonly logger = new Logger(ScheduleApiController.name);
  constructor(private scheduleApiService: ScheduleApiService) {}

  @ApiOperation({ summary: "날짜별 일정 조회" })
  @Get("/daily")
  async getDailySchedule(@Token() token: string, @Query("date") date: Date): Promise<JSON> {
    const result = await this.scheduleApiService.getDailySchedule(token, date);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "주별 일정 조회" })
  @Get("/weekly")
  async getWeeklySchedule(@Token() token: string, @Query("date") date: Date) {
    const result = await this.scheduleApiService.getWeeklySchedule(token, date);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 검색", description: "빈 칸으로 검색 시 전체 조회" })
  @Get("/search")
  async searchByKeyword(@Token() token: string, @Query("keyword") keyword: string): Promise<JSON> {
    this.logger.log("Get /api/schedule/search");
    this.logger.verbose("Keyword: " + keyword);
    const result = await this.scheduleApiService.searchByKeyword(token, keyword);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 추가", description: "categoryUuid=default 는 미분류 카테고리에 추가" })
  @Post("/add")
  async addSchedule(@Token() token: string, @Body() dto: AddScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.addSchedule(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 업데이트", description: "categoryUuid=default 는 미분류 카테고리" })
  @Patch("/update")
  async updateSchedule(@Token() token: string, @Body() dto: UpdateScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.updateSchedule(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "일정 삭제" })
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
  @Get()
  async getScheduleInfo(@Token() token: string, @Query("scheduleUuid") scheduleUuid: string): Promise<JSON> {
    this.logger.log("Get /api/schedule");
    this.logger.verbose("Schedule uuid: " + scheduleUuid);
    const result = await this.scheduleApiService.getSchedule(token, scheduleUuid);
    this.logger.verbose("Result: " + result);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "실패 일정 회고 메모 추가" })
  @Post("/memo")
  async addRetrospectiveMemo(@Token() token: string, @Body() dto: AddRetrospectiveMemoDto): Promise<JSON> {
    this.logger.log("Post /api/schedule/memo");
    this.logger.verbose("Data: " + JSON.stringify(dto, null, 2));
    const result = await this.scheduleApiService.addRetrospectiveMemo(token, dto);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "실패 회고 메모 목록 조회" })
  @Get("/memo")
  async getRetrospectiveMemo(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /api/schedule/memo");
    this.logger.verbose("Token" + token);
    const result = await this.scheduleApiService.getRetrospectiveMemo(token);
    return JSON.parse(result);
  }

  @ApiOperation({ summary: "알람 일정 조회" })
  @Get("/alarm")
  async getScheduleHasAlarm(@Token() token: string): Promise<JSON> {
    this.logger.log("Get /api/schedule/alarm");
    this.logger.verbose("Token: " + token);
    const result = await this.scheduleApiService.getScheduleHasAlarm(token);
    return JSON.parse(result);
  }
}
