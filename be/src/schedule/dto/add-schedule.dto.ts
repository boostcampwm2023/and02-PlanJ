import { IsNotEmpty, IsObject, IsOptional, IsString, Matches } from "class-validator";
import { RepetitionDto } from "./repetition.dto";

export class AddScheduleDto {
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  categoryUuid: string;

  @IsString()
  @IsNotEmpty()
  title: string;

  @Matches(/^(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})$/, { message: "올바른 날짜 및 시간 형식이 아닙니다." })
  endAt: string;

  @IsObject()
  @IsOptional()
  repetition?: RepetitionDto;
}
