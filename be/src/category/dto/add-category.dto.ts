import { IsNotEmpty, IsString, MaxLength } from "class-validator";
import { ApiHideProperty } from "@nestjs/swagger";

export class AddCategoryDto {
  @ApiHideProperty()
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(128)
  categoryName: string;
}
