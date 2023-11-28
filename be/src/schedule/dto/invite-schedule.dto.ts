import { IsNotEmpty, IsObject, IsString, Matches } from "class-validator";
import { ScheduleLocation } from "src/utils/location.interface";

export class InviteScheduleDto {
  @IsString()
  @IsNotEmpty()
  categoryUuid: string;
}
