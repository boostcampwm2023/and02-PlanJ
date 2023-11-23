import {CanActivate, ExecutionContext, Injectable} from "@nestjs/common";
import {Observable} from "rxjs";
import {AuthService} from "../auth/auth.service";
import {UserService} from "../user/user.service";
import {Request} from "express";

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private userService: UserService,
    ) {
    }
    canActivate(context: ExecutionContext): boolean | Promise<boolean> | Observable<boolean> {
        const request = context.switchToHttp().getRequest();
        return this.validateUser(request);
    }

    private async validateUser(request: Request) {
        const jwtToken = request.headers.authorization;
        const userUuid = this.authService.verify(jwtToken);

        return await this.userService.validateUser(userUuid);
    }
}