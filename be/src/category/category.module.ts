import { Module } from "@nestjs/common";
import { CategoryService } from "./category.service";
import { TypeOrmModule } from "@nestjs/typeorm";
import { CategoryEntity } from "./entity/category.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryApiController } from "src/category-api/category-api.controller";
import { CategoryRepository } from "./category.repository";
import { UserCheckRepository } from "./user.check.repository";

@Module({
  imports: [TypeOrmModule.forFeature([CategoryEntity, UserEntity])],
  controllers: [CategoryApiController],
  providers: [CategoryService, CategoryRepository, UserCheckRepository],
  exports: [CategoryService],
})
export class CategoryModule {}
