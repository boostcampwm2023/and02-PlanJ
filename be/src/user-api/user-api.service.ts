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
    };

    return JSON.stringify(body);
  }

  async login(dto: UserLoginDto): Promise<string> {
    const userUuid = await this.userService.login(dto);
    const token = this.authService.issue(userUuid);

    const body: HttpResponse = {
      message: "로그인 성공",
      data: {
        token: token,
      },
    };
    return JSON.stringify(body);
  }

  async delete(token: string): Promise<string> {
    const userUuid = this.authService.verify(token);
    await this.userService.deleteAccount(userUuid);

    const body: HttpResponse = {
      message: "회원탈퇴 완료",
    };
    return JSON.stringify(body);
  }

  async update(dto: UserModifyDto, token: string): Promise<string> {
    const userUuid = this.authService.verify(token);
    const updatedNickname = await this.userService.update(userUuid, dto);

    const result: HttpResponse = {
      message: "정보 수정 성공",
      data: {
        updated_nickname: updatedNickname,
      },
    };
    return JSON.stringify(result);
  }

  async getUserInfo(token: string) {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);

    const result: HttpResponse = {
      message: "회원 정보 조회 성공",
      data: {
        nickname: userEntity.nickname,
        email: userEntity.email,
      },
    };
    return JSON.stringify(result);
  }
}
