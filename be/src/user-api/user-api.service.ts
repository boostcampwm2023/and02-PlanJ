import { Injectable } from "@nestjs/common";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { UserService } from "../user/user.service";
import { AuthService } from "../auth/auth.service";
import { HttpResponse } from "../utils/http.response";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { UserModifyDto } from "../user/dto/user-modify.dto";

@Injectable()
export class UserApiService {
  constructor(
    private userService: UserService,
    private authService: AuthService,
  ) {}

  async register(dto: CreateUserDto): Promise<string> {
    await this.userService.register(dto);
    const body: HttpResponse = {
      message: "회원가입 성공",
      statusCode: 201,
    };

    return JSON.stringify(body);
  }

  async login(dto: UserLoginDto): Promise<string> {
    const userUuid = await this.userService.login(dto);
    const token = this.authService.issue(userUuid);

    const body: HttpResponse = {
      message: "로그인 성공",
      statusCode: 200,
      data: {
        token: token,
      },
    };
    return JSON.stringify(body);
  }

  async delete(token: string) {
    const userUuid = this.authService.verify(token);
    await this.userService.deleteAccount(userUuid);

    const body: HttpResponse = {
      message: "회원탈퇴 완료",
      statusCode: 200,
    };
    return JSON.stringify(body);
  }

  async update(dto: UserModifyDto, token: string) {
    const userUuid = this.authService.verify(token);
    const result = await this.userService.update(userUuid, dto);
    return JSON.stringify(result);
  }
}
