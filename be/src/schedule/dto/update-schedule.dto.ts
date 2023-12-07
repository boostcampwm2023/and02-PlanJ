import { Allow, IsArray, IsNotEmpty, IsObject, IsOptional, IsString, Matches, ValidateNested } from "class-validator";
import { ScheduleLocationDto } from "src/schedule/dto/schedule-location.dto";
import { RepetitionDto } from "./repetition.dto";
import { ScheduleAlarmDto } from "./schedule-alarm.dto";
import { Type } from "class-transformer";
import { ApiHideProperty } from "@nestjs/swagger";

export class UpdateScheduleDto {
  @ApiHideProperty()
  userUuid?: string;

  @IsString()
  @IsNotEmpty()
  categoryUuid: string;

  @IsString()
  @IsNotEmpty()
  scheduleUuid: string;

  @IsString()
  @IsNotEmpty()
  title: string;

  @IsString()
  @IsOptional()
  @Allow()
  description: string | null;

  @Matches(/^(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})$/, { message: "올바른 날짜 및 시간 형식이 아닙니다." })
  @IsOptional()
  @Allow()
  startAt: string | null;

  @Matches(/^(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})$/, { message: "올바른 날짜 및 시간 형식이 아닙니다." })
  endAt: string;

  @ValidateNested({ each: true })
  @Type(() => ScheduleLocationDto)
  @IsOptional()
  startLocation: ScheduleLocationDto;

  @ValidateNested({ each: true })
  @Type(() => ScheduleLocationDto)
  @IsOptional()
  endLocation: ScheduleLocationDto;

  @IsObject()
  @IsOptional()
  repetition?: RepetitionDto;

  @IsArray()
  @IsOptional()
  participants?: string[];

  @ValidateNested({ each: true })
  @Type(() => ScheduleAlarmDto)
  @IsOptional()
  alarm?: ScheduleAlarmDto;
}
