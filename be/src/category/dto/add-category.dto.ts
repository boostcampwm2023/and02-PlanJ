import { IsNotEmpty, IsString, Matches, MaxLength } from "class-validator";

export class AddCategoryDto {
  @IsString()
  @IsNotEmpty()
  userUuid: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(128)
  categoryName: string;

  @Matches(/^(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})$/, { message: "올바른 날짜 및 시간 형식이 아닙니다." })
  createdAt: string;
}
