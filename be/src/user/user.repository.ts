import { CreateUserDto } from "./dto/create-user.dto";
import { DataSource, Repository } from "typeorm";
import { UserEntity } from "./entity/user.entity";
import { ConflictException, Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import * as bcrypt from "bcryptjs";
import { ulid } from "ulid";
import { UserLoginDto } from "./dto/user-login.dto";
import { HttpResponse } from "../utils/http.response";
import { UserModifyDto } from "./dto/user-modify.dto";

@Injectable()
export class UserRepository extends Repository<UserEntity> {
  constructor(dataSource: DataSource) {
    super(UserEntity, dataSource.createEntityManager());
  }

  async register(createUserDto: CreateUserDto): Promise<string> {
    const { email, password, nickname } = createUserDto;

    const userExist = await this.checkUserExists(email);
    if (userExist) {
      throw new ConflictException("해당 이메일로는 가입할 수 없습니다.");
    }

    const userUuid = ulid();

    const salt = await bcrypt.genSalt();
    const hashedPassword = await bcrypt.hash(password, salt);

    const currentDate = new Date();
    const formattedDate = currentDate.toISOString().slice(0, 19).replace("T", " ");

    const user = this.create({
      userUuid: userUuid,
      email: email,
      password: hashedPassword,
      nickname: nickname,
      createdAt: formattedDate,
    });

    try {
      await this.save(user);
      const body: HttpResponse = {
        message: "회원가입 성공",
        statusCode: 201,
        data: {
          userUuid: userUuid,
        },
      };

      return JSON.stringify(body);
    } catch (error) {
      console.log(error);
      throw new InternalServerErrorException();
    }
  }

  private async checkUserExists(email: string) {
    const user = await this.findOne({
      where: { email: email },
    });
    return user !== null;
  }

  async login(userLoginDto: UserLoginDto): Promise<HttpResponse> {
    const { email, password } = userLoginDto;
    const user = await this.findOne({ where: { email: email } });

    if (user && (await bcrypt.compare(password, user.password))) {
      const body: HttpResponse = {
        message: "로그인 성공",
        statusCode: 200,
        data: {
          userUuid: user.userUuid,
        },
      };

      return body;
    }
    throw new UnauthorizedException("로그인 실패");
  }

  async deleteAccount(userLogindto: UserLoginDto): Promise<string> {
    const loginResult = await this.login(userLogindto);
    if (loginResult.statusCode === 200) {
      const { email } = userLogindto;
      await this.softDelete({ email: email });

      const body: HttpResponse = {
        message: "회원탈퇴 완료",
        statusCode: 200,
      };

      return JSON.stringify(body);
    }
  }

  async updateInfo(modifyDto: UserModifyDto) {
    const { email, nickname } = modifyDto;
    const user = await this.findOne({ where: { email: email } });

    if (!user) {
      throw new UnauthorizedException("가입한 사용자가 아님");
    }

    user.nickname = nickname;
    await this.update({ email: email }, { nickname: nickname });

    const body: HttpResponse = {
      message: "정보 수정 성공",
      statusCode: 200,
      data: {
        updatedNickname: nickname,
      },
    };

    return JSON.stringify(body);
  }
}
