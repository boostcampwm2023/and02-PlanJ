import { Module } from "@nestjs/common";
import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { AuthModule } from "./auth/auth.module";
import { TypeOrmModule } from "@nestjs/typeorm";
import { TypeOrmConfigService } from "./config/typeorm.config";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { ScheduleApiModule } from "./schedule-api/schedule-api.module";
import dbConfig from "./config/dbConfig";

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: `${__dirname}/config/env/.${process.env.NODE_ENV}.env`,
      load: [dbConfig],
    }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => TypeOrmConfigService.createTypeOrmOptions(configService),
    }),
    AuthModule,
    ScheduleApiModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
