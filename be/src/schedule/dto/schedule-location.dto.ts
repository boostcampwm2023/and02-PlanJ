import { IsDecimal, IsOptional, IsString } from "class-validator";

export class ScheduleLocationDto {
  @IsOptional()
  @IsString()
  placeName: string | null;

  @IsOptional()
  @IsString()
  placeAddress: string | null;

  @IsOptional()
  @IsDecimal()
  latitude: number | null;

  @IsOptional()
  @IsDecimal()
  longitude: number | null;
}
