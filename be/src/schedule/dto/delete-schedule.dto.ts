import { IsNotEmpty, IsString } from "class-validator";

export class DeleteScheduleDto {
  @IsString()
  @IsNotEmpty()
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  scheduleUuid: string;
}
