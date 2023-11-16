import { IsDate, IsDateString, IsNotEmpty, IsString, Matches } from "class-validator";

export class AddScheduleDto {
  @IsString()
  @IsNotEmpty()
  userId: string;

  @IsString()
  @IsNotEmpty()
  title: string;

  @IsString()
  description: string;

  @Matches(/^\d{2}:\d{2}:\d{2}$/, { message: "올바른 시간 형식이 아닙니다. (HH:MM:SS)" })
  startTime: string;

  @Matches(/^\d{2}:\d{2}:\d{2}$/, { message: "올바른 시간 형식이 아닙니다. (HH:MM:SS)" })
  endTime: string;
}
