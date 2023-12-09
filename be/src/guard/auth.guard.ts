import { CanActivate, ExecutionContext, Injectable, Logger } from "@nestjs/common";
import { Observable } from "rxjs";
import { AuthService } from "../auth/auth.service";
import { UserService } from "../user/user.service";
import { Request } from "express";

@Injectable()
export class AuthGuard implements CanActivate {
  private readonly logger = new Logger(AuthGuard.name);

  constructor(
    private authService: AuthService,
    private userService: UserService,
  ) {}

  canActivate(context: ExecutionContext): boolean | Promise<boolean> | Observable<boolean> {
    const request = context.switchToHttp().getRequest();
    return this.validateUser(request);
  }

  private async validateUser(request: Request) {
    const token = request.headers.authorization;
    const replacedToken = token.replace(/^Bearer /i, "");
    this.logger.verbose("Token: " + replacedToken);
    try {
      const userUuid = this.authService.verify(replacedToken);
      return await this.userService.validateUser(userUuid);
    } catch (e) {
      this.logger.error(e);
    }
  }
}
