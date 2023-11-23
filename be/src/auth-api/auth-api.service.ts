import {Injectable} from "@nestjs/common";
import {UserLoginDto} from "../user/dto/user-login.dto";
import {UserService} from "../user/user.service";
import {AuthService} from "../auth/auth.service";
import {HttpResponse} from "../utils/http.response";

@Injectable()
export class AuthApiService {
    constructor(
        private userService: UserService,
        private authService: AuthService
    ) {
    }

    async login(dto: UserLoginDto): Promise<string> {
        const user = await this.userService.login(dto);
        const token = this.authService.issue(JSON.parse(user));

        const body: HttpResponse = {
            message: "로그인 성공",
            statusCode: 200,
            data: {
                token: token
            },
        };
        return JSON.stringify(body);
    }
}