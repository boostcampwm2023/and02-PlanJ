import { IsEmail, IsString, MaxLength, MinLength } from "class-validator";

export class UserModifyDto {
  @IsString()
  @MinLength(2)
  @MaxLength(12)
  nickname: string;
}
