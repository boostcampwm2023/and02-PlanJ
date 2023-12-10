import { Module } from "@nestjs/common";
import { PushService } from "./push.service";

@Module({
  imports: [],
  providers: [PushService],
  exports: [PushService],
})
export class PushModule {}
