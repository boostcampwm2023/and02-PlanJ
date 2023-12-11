import { Injectable, Logger } from "@nestjs/common";
import { UserLoginDto } from "../user/dto/user-login.dto";
import { UserService } from "../user/user.service";
import { AuthService } from "../auth/auth.service";
import { HttpResponse } from "../utils/http.response";
import { CreateUserDto } from "../user/dto/create-user.dto";
import { ImageService } from "../image/image.service";
import axios from "axios";
import { NaverResponseDto } from "../user/dto/naver-response.dto";

@Injectable()
export class UserApiService {
  private readonly logger = new Logger(UserApiService.name);
  constructor(
    private userService: UserService,
    private authService: AuthService,
    private imageService: ImageService,
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
    if (!!dto.deviceToken) {
      await this.userService.saveDeviceToken(userUuid, dto.deviceToken);
    }

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

  async getUserInfo(token: string) {
    const userUuid = this.authService.verify(token);
    const userEntity = await this.userService.getUserEntity(userUuid);

    const result: HttpResponse = {
      message: "회원 정보 조회 성공",
      data: {
        nickname: userEntity.nickname,
        email: userEntity.email,
        profileUrl: userEntity.profileUrl,
      },
    };
    return JSON.stringify(result);
  }

  async updateUserInfo(token: string, profileImage: Express.Multer.File, nickname: string): Promise<string> {
    const userUuid = this.authService.verify(token);
    let profileImageUrl = null;

    // 사용자가 파일을 업로드 했을 때
    if (!!profileImage) {
      profileImageUrl = await this.imageService.uploadImage(profileImage);
    }

    await this.userService.update(userUuid, nickname, profileImageUrl);

    const result: HttpResponse = {
      message: "정보 수정 성공",
    };
    return JSON.stringify(result);
  }

  async loginByNaver(accessToken: string, deviceToken: string) {
    const response = await axios.get("https://openapi.naver.com/v1/nid/me", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    this.logger.verbose(response.data);
    const data = response.data.response;
    const naverResponse: NaverResponseDto = {
      id: data.id,
      nickname: data.nickname,
      profileImage: data.profile_image,
      email: data.email,
    };

    const userEntity = await this.userService.loginByNaver(naverResponse, deviceToken);
    const token = this.authService.issue(userEntity.userUuid);

    const body: HttpResponse = {
      message: "네이버 로그인 성공",
      data: {
        token: token,
      },
    };
    return JSON.stringify(body);
  }

  async setProfileImageDefault(token: string) {
    const userUuid = this.authService.verify(token);
    await this.userService.setUserProfileImageNull(userUuid);

    const body: HttpResponse = {
      message: "프로필 이미지를 기본 이미지로 변경 하였습니다.",
    };
    return JSON.stringify(body);
  }

  async logout(token: string) {
    const userUuid = this.authService.verify(token);
    await this.userService.logout(userUuid);

    const body: HttpResponse = {
      message: "로그아웃 성공",
    };
    return JSON.stringify(body);
  }
}
