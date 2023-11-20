import { ScheduleService } from "../schedule/schedule.service";
import { Controller, Post, Get, Patch, Delete, Query, Body } from "@nestjs/common";
import { AddScheduleDto } from "../schedule/dto/add-schedule.dto";

@Controller("/api/schedule")
export class ScheduleApiController {
  constructor(private scheduleService: ScheduleService) {}

  @Get("/daily")
  getDailySchedule(@Query("userId") userId: string, @Query("date") date: Date) {
    console.log(userId);
    console.log(date.toString());
  }

  @Get("/weekly")
  getWeeklySchedule() {}

  @Post("/add")
  async addSchedule(@Body() dto: AddScheduleDto): Promise<JSON> {
    console.log("스케줄api컨트롤러");
    const result = await this.scheduleService.addSchedule(dto);
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
