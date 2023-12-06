import { IsNumber, IsOptional, IsString } from "class-validator";

export class ScheduleLocationDto {
  @IsOptional()
  @IsString()
  placeName: string | null;

  @IsOptional()
  @IsString()
  placeAddress: string | null;

  @IsOptional()
  @IsNumber()
  latitude: number | null;

  @IsOptional()
  @IsNumber()
  longitude: number | null;
}
