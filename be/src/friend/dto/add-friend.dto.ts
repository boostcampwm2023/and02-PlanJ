import { IsNotEmpty, IsString } from "class-validator";

export class AddFriendDto {
  @IsString()
  @IsNotEmpty()
  from: string;

  @IsString()
  @IsNotEmpty()
  to: string;
}
