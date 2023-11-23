import {Inject, Injectable} from '@nestjs/common';
import {ConfigType} from "@nestjs/config";
import * as jwt from "jsonwebtoken";
import authConfig from "../config/auth.config";

@Injectable()
export class AuthService {
    constructor(
        @Inject(authConfig.KEY) private config: ConfigType<typeof authConfig>
    ) {}

    issue(userInfo: JSON) {
        const payload = { userUuid: userInfo["data"]["userUuid"] };

        return jwt.sign(payload, this.config.jwtSecret, {
            expiresIn: '1m',
            audience: 'test.com',
            issuer: 'test.com'
        })
    }
}
