import { Module } from "@nestjs/common";
import { CategoryService } from "./category.service";

@Module({
  providers: [CategoryService],
})
export class CategoryModule {}
