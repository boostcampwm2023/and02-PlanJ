import { IsEmail, IsString, MaxLength, MinLength } from "class-validator";

export class UserModifyDto {
  @IsString()
  @IsEmail()
  email: string;

  @IsString()
  @MinLength(2)
  @MaxLength(12)
  nickname: string;
}
