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

  async checkUserExists(email: string) {
    const user = await this.findOne({
      where: { email: email },
    });
    return user !== null;
  }

  async deleteUser(userLoginDto: UserLoginDto): Promise<string> {
    // const loginResult = await this.loginUser(userLoginDto);
    // if (loginResult.statusCode === 200) {
      const { email } = userLoginDto;
      await this.softDelete({ email: email });

      const body: HttpResponse = {
        message: "회원탈퇴 완료",
        statusCode: 200,
      };

      return JSON.stringify(body);
    // }
  }

  async updateUser(userUuid: string, modifyDto: UserModifyDto) {
    const { nickname } = modifyDto;
    const user = await this.findOne({ where: { userUuid: userUuid } });

    user.nickname = nickname;

    await this.save(user);

    const body: HttpResponse = {
      message: "정보 수정 성공",
      statusCode: 200,
      data: {
        updatedNickname: nickname,
      },
    };

    return JSON.stringify(body);
  }

  async checkByUserUuid(userUuid: string): Promise<UserEntity> {
    return await this.getUserEntity(userUuid);
  }

  private async getUserEntity(userUuid: string) {
    const user = await this.findOne({
      where: { userUuid: userUuid },
    });

    if (user === null) {
      throw new UnauthorizedException("존재하지 않는 user");
    }
    return user;
  }
}
