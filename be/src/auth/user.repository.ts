import { CreateUserDto } from "./dto/create-user.dto";
import { DataSource, Repository } from "typeorm";
import { UserEntity } from "./user.entity";
import { ConflictException, Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import * as bcrypt from "bcryptjs";
import { ulid } from "ulid";
import { UserLoginDto } from "./dto/user-login.dto";

@Injectable()
export class UserRepository extends Repository<UserEntity> {
  constructor(dataSource: DataSource) {
    super(UserEntity, dataSource.createEntityManager());
  }

  async register(createUserDto: CreateUserDto): Promise<string> {
    const { email, password, nickname } = createUserDto;

    const salt = await bcrypt.genSalt();
    const hashedPassword = await bcrypt.hash(password, salt);

    const user = this.create({ userId: ulid(), email: email, password: hashedPassword, nickname: nickname });

    try {
      await this.save(user);
      return `${email}님 회원가입 완료되었습니다.`;
    } catch (error) {
      if (error.code === "23505") {
        throw new ConflictException("이미 존재하는 유저이름입니다.");
      }
      throw new InternalServerErrorException();
    }
  }

  async login(loginDto: UserLoginDto): Promise<string> {
    const { email, password } = loginDto;
    const user = await this.findOne({ where: { email: email } });

    if (user && (await bcrypt.compare(password, user.password))) {
      return "login success";
    }
    throw new UnauthorizedException("login failed");
  }
}
