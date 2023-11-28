import { IsNotEmpty, IsString } from "class-validator";

export class InviteScheduleDto {
  @IsString()
  @IsNotEmpty()
  categoryUuid: string;
}
