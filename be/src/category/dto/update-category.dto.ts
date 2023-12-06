import { IsNotEmpty, IsString, MaxLength } from "class-validator";
import { ApiHideProperty } from "@nestjs/swagger";

export class UpdateCategoryDto {
  @ApiHideProperty()
  userId: number;

  @IsString()
  @IsNotEmpty()
  categoryUuid: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(128)
  categoryName: string;
}
