import { Module } from "@nestjs/common";
import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { TypeOrmModule } from "@nestjs/typeorm";
import { TypeOrmConfigService } from "./config/typeorm.config";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { ScheduleApiModule } from "./schedule-api/schedule-api.module";
import { AuthApiModule } from "./auth-api/auth-api.module";
import { CategoryApiModule } from "./category-api/category-api.module";
import { FriendModule } from './friend/friend.module';
import { AuthModule } from './auth/auth.module';
import dbConfig from "./config/db.config";
import authConfig from "./config/auth.config";

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: `${__dirname}/config/env/.${process.env.NODE_ENV}.env`,
      load: [dbConfig, authConfig],
    }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => TypeOrmConfigService.createTypeOrmOptions(configService),
    }),
    ScheduleApiModule,
    AuthApiModule,
    CategoryApiModule,
    FriendModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
