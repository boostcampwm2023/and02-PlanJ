import { IsNotEmpty, IsString } from "class-validator";

export class DeleteCategoryDto {
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  categoryUuid: string;
}
