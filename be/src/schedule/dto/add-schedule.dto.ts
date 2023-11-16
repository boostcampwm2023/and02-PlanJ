import { IsString } from "class-validator";

export class AddScheduleDto {
  @IsString()
  userId: string;
}
