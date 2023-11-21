import { CategoryApiService } from "./../category-api/category-api.service";
import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { CategoryEntity } from "./entity/category.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryApiController } from "src/category-api/category-api.controller";
import { CategoryRepository } from "./category.repository";
import { CategoryService } from "./category.service";

@Module({
  imports: [TypeOrmModule.forFeature([CategoryEntity, UserEntity])],
  controllers: [CategoryApiController],
  providers: [CategoryRepository, CategoryApiService],
  exports: [CategoryService],
})
export class CategoryModule {}
