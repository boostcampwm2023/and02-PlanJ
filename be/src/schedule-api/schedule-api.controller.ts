import { Controller, Post, Get, Patch, Delete, Query, Body } from "@nestjs/common";
import { AddScheduleDto } from "../schedule/dto/add-schedule.dto";
import { ScheduleApiService } from "./schedule-api.service";

@Controller("/api/schedule")
export class ScheduleApiController {
  constructor(private scheduleApiService: ScheduleApiService) {}

  // @Get("/daily")
  // async getDailySchedule(@Query("userUuid") userUuid: string, @Query("date") date: Date) {
  //   const result = await this.scheduleApiService.getDailySchedule(userUuid, date);
  //   return JSON.parse(result);
  // }

  @Get("/weekly")
  getWeeklySchedule() {}

  @Post("/add")
  async addSchedule(@Body() dto: AddScheduleDto): Promise<JSON> {
    const result = await this.scheduleApiService.addSchedule(dto);
    return JSON.parse(result);
  }

  @Patch("/update")
  updateSchedule() {}

  @Delete("/delete")
  deleteSchedule() {}

  @Post("/participate")
  participateSchedule() {}

  // TODO: 지도표시
}
