import { IsNotEmpty, IsString, MaxLength } from "class-validator";

export class UpdateCategoryDto {
  userId: number;

  @IsString()
  @IsNotEmpty()
  categoryUuid: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(128)
  categoryName: string;
}
