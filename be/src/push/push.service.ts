import { Injectable, InternalServerErrorException, Logger } from "@nestjs/common";
import * as admin from "firebase-admin";
import firebaseConfig from "../config/firebase.config";

// TODO: User provider
admin.initializeApp({
  credential: admin.credential.cert(firebaseConfig),
});

@Injectable()
export class PushService {
  private readonly logger = new Logger(PushService.name);
  private readonly title: string = "PlanJ";

  constructor() {}

  async sendPush(token: string, body: string) {
    const payload = {
      token: token,
      notification: {
        title: this.title,
        body: body,
      },
    };

    this.logger.verbose("Payload" + JSON.stringify(payload, null, 2));
    try {
      await admin.messaging().send(payload);
    } catch (e) {
      this.logger.error(e);
      throw new InternalServerErrorException(e.message);
    }
  }
}
