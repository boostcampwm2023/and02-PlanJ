import { Controller, Post, Get, Patch, Delete, Query, Body, UseGuards, Logger } from "@nestjs/common";
import { AddScheduleDto } from "../schedule/dto/add-schedule.dto";
import { ScheduleApiService } from "./schedule-api.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";
import { DeleteScheduleDto } from "src/schedule/dto/delete-schedule.dto";
import { Token } from "../utils/token.decorator";
import { AuthGuard } from "../guard/auth.guard";
import { AddRetrospectiveMemoDto } from "./dto/add-retrospective-memo.dto";

@Controller("/api/schedule")
@UseGuards(AuthGuard)
export class ScheduleApiController {
  private readonly logger = new Logger(ScheduleApiController.name);
  constructor(private scheduleApiService: ScheduleApiService) {}

  @Get("/daily")
  async getDailySchedule(@Token() token: string, @Query("date") date: Date): Promise<JSON> {
    const result = await this.scheduleApiService.getDailySchedule(token, date);
    return JSON.parse(result);
  }

  @Get("/weekly")
  async getWeeklySchedule(@Token() token: string, @Query("date") date: Date) {
    const result = await this.scheduleApiService.getWeeklySchedule(token, date);
    return JSON.parse(result);
  }

  @Post("/add")
  async addSchedule(@Token() token: string, @Body() dto: AddScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.addSchedule(token, dto);
    return JSON.parse(result);
  }

  @Patch("/update")
  async updateSchedule(@Token() token: string, @Body() dto: UpdateScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.updateSchedule(token, dto);
    return JSON.parse(result);
  }

  @Delete("/delete")
  async deleteSchedule(@Token() token: string, @Body() dto: DeleteScheduleDto) {
    const result = await this.scheduleApiService.deleteSchedule(token, dto);
    return JSON.parse(result);
  }

  @Get("/check")
  async checkedSchedule(@Token() token: string, @Query("scheduleUuid") scheduleUuid: string): Promise<JSON> {
    const result = await this.scheduleApiService.checkedSchedule(scheduleUuid);
    return JSON.parse(result);
  }

  @Get()
  async getScheduleInfo(@Token() token: string, @Query("scheduleUuid") scheduleUuid: string): Promise<JSON> {
    this.logger.log("Get /api/schedule");
    this.logger.verbose("Schedule uuid: " + scheduleUuid);
    const result = await this.scheduleApiService.getSchedule(token, scheduleUuid);
    this.logger.verbose("Result: " + result);
    return JSON.parse(result);
  }

  @Post("/add-memo")
  async addRetrospectiveMemo(@Token() token: string, @Body() dto: AddRetrospectiveMemoDto): Promise<JSON> {
    this.logger.log("Post /api/schedule");
    this.logger.verbose("Data: " + JSON.stringify(dto, null, 2));
    const result = await this.scheduleApiService.addRetrospectiveMemo(token, dto);
    return JSON.parse(result);
  }
}
