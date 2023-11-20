import { Module } from "@nestjs/common";
import { CategoryApiController } from "./category-api.controller";
import { ScheduleModule } from "src/schedule/schedule.module";
import { UserModule } from "src/user/user.module";
import { ScheduleMetaDataModule } from "src/schedule-meta-data/schedule-meta-data.module";
import { CategoryModule } from "src/category/category.module";
import { CategoryApiService } from "./category-api.service";

@Module({
  imports: [ScheduleModule, ScheduleMetaDataModule, CategoryModule, UserModule],
  controllers: [CategoryApiController],
  providers: [CategoryApiService],
})
export class CategoryApiModule {}
