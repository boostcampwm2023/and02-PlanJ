import { Injectable, Logger, OnApplicationShutdown } from "@nestjs/common";
import { GlobalService } from "./disable-connection.global";

@Injectable()
class ShutDown implements OnApplicationShutdown {
  private readonly logger = new Logger(ShutDown.name);
  onApplicationShutdown(signal: string) {
    if (signal === "SIGINT") {
      GlobalService.isDisableConnection = true;
      this.logger.log("Shut down server...");
    }
  }
}
