import {Inject, Injectable, UnauthorizedException} from '@nestjs/common';
import {ConfigType} from "@nestjs/config";
import * as jwt from "jsonwebtoken";
import authConfig from "../config/auth.config";

@Injectable()
export class AuthService {
    constructor(
        @Inject(authConfig.KEY) private config: ConfigType<typeof authConfig>
    ) {}

    issue(userUuid: string) {
        const payload = { userUuid: userUuid };
        const options = {
            expiresIn: this.config.options.expiresIn,
            issuer: this.config.options.issuer
        }
        return jwt.sign(payload, this.config.jwtSecret, options)
    }

    verify(jwtToken: string) {
        try {
            const payload = jwt.verify(jwtToken, this.config.jwtSecret) as (jwt.JwtPayload)
            const { userUuid } = payload;
            return userUuid;
        } catch (e) {
            throw new UnauthorizedException("유효하지 않은 사용자");
        }
    }
}
