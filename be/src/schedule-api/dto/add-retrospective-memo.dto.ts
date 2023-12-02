import { IsString } from "class-validator";

export class AddRetrospectiveMemoDto {
  @IsString()
  scheduleUuid: string;

  @IsString()
  retrospectiveMemo: string;
}
