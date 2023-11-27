import { IsNotEmpty, IsString, MaxLength } from "class-validator";

export class AddCategoryDto {
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(128)
  categoryName: string;
}
