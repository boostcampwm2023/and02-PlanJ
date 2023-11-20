import { Module } from "@nestjs/common";
import { CategoryApiController } from "./category-api.controller";
import { ScheduleModule } from "src/schedule/schedule.module";
import { UserModule } from "src/user/user.module";
import { ScheduleMetaDataModule } from "src/schedule-meta-data/schedule-meta-data.module";
import { CategoryModule } from "src/category/category.module";

@Module({
  imports: [ScheduleModule, ScheduleMetaDataModule, CategoryModule, UserModule],
  controllers: [CategoryApiController],
})
export class CategoryApiModule {}
