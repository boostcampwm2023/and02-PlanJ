import { IsNotEmpty, IsString } from "class-validator";

export class DeleteCategoryDto {
  @IsString()
  @IsNotEmpty()
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  categoryUuid: string;
}
