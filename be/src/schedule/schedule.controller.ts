import { ScheduleService } from "./schedule.service";
import { Controller, Post, Get, Patch, Delete, Query, Body } from "@nestjs/common";
import { AddScheduleDto } from "./dto/add-schedule.dto";

@Controller("/api/schedule")
export class ScheduleController {
  constructor(private scheduleService: ScheduleService) {}

  @Get("/daily")
  getDailySchedule(@Query("userId") userId: string, @Query("date") date: Date) {
    console.log(userId);
    console.log(date.toString());
  }

  @Get("/weekly")
  getWeeklySchedule() {}

  @Post("/add")
  addSchedule(@Body() dto: AddScheduleDto) {
    return this.scheduleService.addSchedule(dto);
  }

  @Patch("/update")
  updateSchedule() {}

  @Delete("/delete")
  deleteSchedule() {}

  @Post("/participate")
  participateSchedule() {}

  // TODO: 지도표시
}
