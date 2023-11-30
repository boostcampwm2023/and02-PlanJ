import { IsOptional, IsString, Matches } from "class-validator";

export class ScheduleAlarmDto {
  @IsOptional()
  @IsString()
  alarmType: string;

  @IsOptional()
  @IsString()
  @Matches(/^(\d{2}:\d{2}:\d{2})$/, { message: "올바른 날짜 및 시간 형식이 아닙니다." })
  alarmTime: string;
}
