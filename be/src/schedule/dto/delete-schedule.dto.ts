import { IsNotEmpty, IsString } from "class-validator";

export class DeleteScheduleDto {
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  scheduleUuid: string;
}
