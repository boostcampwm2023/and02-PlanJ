import { IsDecimal, IsString } from "class-validator";

export class RepetitionDto {
  @IsString()
  cycleType: string;

  @IsDecimal()
  cycleCount: number;
}
