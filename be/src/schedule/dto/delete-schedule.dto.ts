import { IsNotEmpty, IsString, Matches } from "class-validator";

export class DeleteScheduleDto {
  @IsString()
  @IsNotEmpty()
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  scheduleUuid: string;
}
