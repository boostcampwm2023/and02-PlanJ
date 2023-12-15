import { Injectable, Logger } from "@nestjs/common";
import * as admin from "firebase-admin";
import firebaseConfig from "../config/firebase.config";
import { PayloadDto } from "./dto/payload.dto";

// TODO: Use provider
admin.initializeApp({
  credential: admin.credential.cert(firebaseConfig),
});

@Injectable()
export class PushService {
  private readonly logger = new Logger(PushService.name);
  private readonly title: string = "PlanJ";

  constructor() {}

  async sendPush(token: string, body: string, index: string = undefined) {
    const payload: PayloadDto = {
      token: token,
      data: {
        title: this.title,
        body: body,
        index: index,
      },
    };

    this.logger.verbose("Payload" + JSON.stringify(payload, null, 2));
    try {
      await admin.messaging().send(payload);
    } catch (e) {
      this.logger.error(e);
    }
  }
}
