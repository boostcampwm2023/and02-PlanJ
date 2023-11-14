import { TypeOrmModuleOptions } from "@nestjs/typeorm";

export const typeORMConfig: TypeOrmModuleOptions = {
  type: "mysql",
  host: "localhost",
  port: 3306,
  username: "root",
  password: "boostcamp",
  database: "test",
  entities: [__dirname + "/../**/*.entity.{js,ts}"],
  synchronize: true,
};
