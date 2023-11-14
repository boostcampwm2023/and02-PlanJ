import { IsString, IsEmail, MaxLength, MinLength } from "class-validator";

export class UserLoginDto {
  @IsString()
  @IsEmail()
  @MaxLength(60)
  email: string;

  @IsString()
  @MinLength(8)
  @MaxLength(16)
  password: string;
}
