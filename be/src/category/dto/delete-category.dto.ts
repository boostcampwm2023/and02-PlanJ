import { IsNotEmpty, IsString } from "class-validator";
import { ApiHideProperty } from "@nestjs/swagger";

export class DeleteCategoryDto {
  @ApiHideProperty()
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  categoryUuid: string;
}
