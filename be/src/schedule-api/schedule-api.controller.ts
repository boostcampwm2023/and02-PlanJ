import { Controller, Post, Get, Patch, Delete, Query, Body } from "@nestjs/common";
import { AddScheduleDto } from "../schedule/dto/add-schedule.dto";
import { ScheduleApiService } from "./schedule-api.service";
import { UpdateScheduleDto } from "src/schedule/dto/update-schedule.dto";
import { DeleteScheduleDto } from "src/schedule/dto/delete-schedule.dto";

@Controller("/api/schedule")
export class ScheduleApiController {
  constructor(private scheduleApiService: ScheduleApiService) {}

  @Get("/daily")
  async getDailySchedule(@Query("userUuid") userUuid: string, @Query("date") date: Date): Promise<JSON> {
    const result = await this.scheduleApiService.getDailySchedule(userUuid, date);
    return JSON.parse(result);
  }

  @Get("/weekly")
  async getWeeklySchedule(@Query("userUuid") userUuid: string, @Query("date") date: Date) {
    const result = await this.scheduleApiService.getWeeklySchedule(userUuid, date);
    return JSON.parse(result);
  }

  @Post("/add")
  async addSchedule(@Body() dto: AddScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.addSchedule(dto);
    return JSON.parse(result);
  }

  @Patch("/update")
  async updateSchedule(@Body() dto: UpdateScheduleDto) {
    const result = await this.scheduleApiService.updateSchedule(dto);
    return JSON.parse(result);
  }

  @Delete("/delete")
  async deleteSchedule(@Body() dto: DeleteScheduleDto) {
    const result = await this.scheduleApiService.deleteSchedule(dto);
    return JSON.parse(result);
  }

  @Post("/participate")
  participateSchedule() {}

  // TODO: 지도표시
}
