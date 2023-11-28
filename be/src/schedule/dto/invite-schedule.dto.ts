import { IsEmail, IsNotEmpty, IsString } from "class-validator";

export class InviteScheduleDto {
  @IsString()
  @IsNotEmpty()
  scheduleUuid: string;

  @IsEmail()
  @IsNotEmpty()
  invitedUserEmail: string;
}
