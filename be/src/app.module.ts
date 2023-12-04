import { Module } from "@nestjs/common";
import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { TypeOrmModule } from "@nestjs/typeorm";
import { TypeOrmConfigService } from "./config/typeorm.config";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { ScheduleApiModule } from "./schedule-api/schedule-api.module";
import { UserApiModule } from "./user-api/user-api.module";
import { CategoryApiModule } from "./category-api/category-api.module";
import { FriendModule } from "./friend/friend.module";
import dbConfig from "./config/db.config";
import authConfig from "./config/auth.config";
import awsConfig from "./config/aws.config";
import greenEyeConfig from "./config/green-eye.config";
import { validationSchema } from "./config/validation-schema";

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: `${__dirname}/config/env/.${process.env.NODE_ENV}.env`,
      load: [dbConfig, authConfig, awsConfig, greenEyeConfig],
      validationSchema,
    }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => TypeOrmConfigService.createTypeOrmOptions(configService),
    }),
    ScheduleApiModule,
    UserApiModule,
    CategoryApiModule,
    FriendModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
