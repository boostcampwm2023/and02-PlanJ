import { CreateUserDto } from "./dto/create-user.dto";
import { DataSource, Repository } from "typeorm";
import { UserEntity } from "./user.entity";
import { ConflictException, Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import * as bcrypt from "bcryptjs";
import { ulid } from "ulid";
import { UserLoginDto } from "./dto/user-login.dto";
import { HttpResponse } from "../utils/http.response";

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

    const salt = await bcrypt.genSalt();
    const hashedPassword = await bcrypt.hash(password, salt);

    const user = this.create({ userId: ulid(), email: email, password: hashedPassword, nickname: nickname });

    try {
      await this.save(user);
      const body: HttpResponse = {
        message: "회원가입 성공",
        statusCode: 201,
      }

      return JSON.stringify(body);
    } catch (error) {
      throw new InternalServerErrorException();
    }
  }

  private async checkUserExists(email: string) {
    const user = await this.findOne({
      where: { email: email },
    });
    return user !== null;
  }

  async login(loginDto: UserLoginDto): Promise<string> {
    const { email, password } = loginDto;
    const user = await this.findOne({ where: { email: email } });

    if (user && (await bcrypt.compare(password, user.password))) {
      const body: HttpResponse = {
        message: "로그인 성공",
        statusCode: 200
      }

      return JSON.stringify(body);
    }
    throw new UnauthorizedException("login failed");
  }
}
