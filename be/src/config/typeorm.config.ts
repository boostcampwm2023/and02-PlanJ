import { ConfigService, ConfigType } from "@nestjs/config";
import { TypeOrmModuleOptions } from "@nestjs/typeorm";
import dbConfig from "./db.config";

export class TypeOrmConfigService {
  static createTypeOrmOptions(configService: ConfigService): TypeOrmModuleOptions {
    const config: ConfigType<typeof dbConfig> = configService.get("db");
    return {
      type: "mysql",
      host: config.host,
      port: config.port,
      username: config.username,
      password: config.password,
      database: config.database,
      entities: [__dirname + "/../**/*.entity{.ts,.js}"],
      synchronize: false,
      charset: "utf8mb4",
    };
  }
}
