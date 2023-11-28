import { IsNotEmpty, IsObject, IsString, Matches } from "class-validator";

export class InviteScheduleDto {
  @IsString()
  @IsNotEmpty()
  categoryUuid: string;
}
