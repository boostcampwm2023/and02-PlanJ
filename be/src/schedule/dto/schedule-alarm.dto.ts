import { IsInt, IsString, Max, Min } from "class-validator";

export class ScheduleAlarmDto {
  @IsString()
  alarmType: string;

  @IsInt()
  @Max(99)
  @Min(0)
  alarmTime: number;

  @IsString()
  firstScheduleUuid: string;
}
