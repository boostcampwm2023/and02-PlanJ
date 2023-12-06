import { IsNotEmpty, IsString } from "class-validator";
import { ApiHideProperty } from "@nestjs/swagger";

export class DeleteScheduleDto {
  @ApiHideProperty()
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  scheduleUuid: string;
}
