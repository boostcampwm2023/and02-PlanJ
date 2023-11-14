import { Module } from "@nestjs/common";
import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { AuthModule } from "./auth/auth.module";
import { TypeOrmModule } from "@nestjs/typeorm";
import { typeORMConfig } from "./config/typeorm.config";

@Module({
  imports: [TypeOrmModule.forRoot(typeORMConfig), AuthModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
