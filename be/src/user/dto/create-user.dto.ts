import { IsEmail, IsString, MaxLength, MinLength } from "class-validator";

export class CreateUserDto {
  @IsEmail()
  @IsString()
  email: string;

  @IsString()
  @MinLength(8)
  @MaxLength(16)
  password: string;

  @IsString()
  @MinLength(2)
  @MaxLength(12)
  nickname: string;
}
