import { IsNumberString, IsOptional, IsString } from "class-validator";

export class ScheduleLocationDto {
  @IsOptional()
  @IsString()
  placeName: string | null;

  @IsOptional()
  @IsString()
  placeAddress: string | null;

  @IsOptional()
  @IsNumberString()
  latitude: number | null;

  @IsOptional()
  @IsNumberString()
  longitude: number | null;
}
