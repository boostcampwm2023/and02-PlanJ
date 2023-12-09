import { IsInt, IsOptional, IsString, Max, Min } from "class-validator";

export class ScheduleAlarmDto {
  @IsString()
  alarmType: string;

  @IsInt()
  @Max(99)
  @Min(0)
  alarmTime: number;

  @IsInt()
  @IsOptional()
  estimatedTime: number;
}
