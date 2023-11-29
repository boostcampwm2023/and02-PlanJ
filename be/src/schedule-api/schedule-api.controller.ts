import { Controller, Post, Get, Patch, Delete, Query, Body, UseGuards } from "@nestjs/common";
import { AddScheduleDto } from "../schedule/dto/add-schedule.dto";
import { ScheduleApiService } from "./schedule-api.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";
import { DeleteScheduleDto } from "src/schedule/dto/delete-schedule.dto";
import { Token } from "../utils/token.decorator";
import { AuthGuard } from "../guard/auth.guard";
import { InviteScheduleDto } from "src/schedule/dto/invite-schedule.dto";

@Controller("/api/schedule")
@UseGuards(AuthGuard)
export class ScheduleApiController {
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
}
