import {Controller, Post, Get, Patch, Delete, Query} from "@nestjs/common";

@Controller("/api/schedule")
export class ScheduleController {
    @Get("/daily")
    getDailySchedule(@Query('userId') userId: string, @Query('date') date: Date) {
        console.log(userId);
        console.log(date.toString());
    }

    @Get("/weekly")
    getWeeklySchedule() {

    }

    @Post("/add")
    addSchedule() {

    }

    @Patch("/update")
    updateSchedule() {

    }

    @Delete("/delete")
    deleteSchedule() {

    }

    @Post("/participate")
    participateSchedule() {

    }

    // TODO: 지도표시
}
