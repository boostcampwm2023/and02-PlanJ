import { Allow, IsNotEmpty, IsOptional, IsString, Matches } from "class-validator";

export class UpdateScheduleDto {
  @IsString()
  @IsNotEmpty()
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

  @IsString()
  @IsOptional()
  @Allow()
  placeName: string | null;

  @IsString()
  @IsOptional()
  @Allow()
  placeAddress: string | null;

  @IsString()
  @IsOptional()
  @Allow()
  latitude: string | null;

  @IsString()
  @IsOptional()
  @Allow()
  longtitude: string | null;
}
