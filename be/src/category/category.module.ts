import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { CategoryEntity } from "./entity/category.entity";
import { CategoryService } from "./category.service";
import { CategoryRepository } from "./category.repository";

@Module({
  imports: [TypeOrmModule.forFeature([CategoryEntity])],
  controllers: [],
  providers: [CategoryService, CategoryRepository],
  exports: [CategoryService],
})
export class CategoryModule {}
