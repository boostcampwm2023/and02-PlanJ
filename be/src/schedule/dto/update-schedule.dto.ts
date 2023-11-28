import { Allow, IsNotEmpty, IsObject, IsOptional, IsString, Matches } from "class-validator";
import { ScheduleLocationDto } from "src/schedule/dto/schedule-location.dto";
import {RepetitionDto} from "./repetition.dto";

export class UpdateScheduleDto {
  userUuid: string;

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

  @IsObject()
  @IsOptional()
  startLocation: ScheduleLocationDto;

  @IsObject()
  @IsOptional()
  endLocation: ScheduleLocationDto;

  @IsObject()
  @IsOptional()
  repetition: RepetitionDto;
}
