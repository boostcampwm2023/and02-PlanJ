import { CategoryApiService } from "./../category-api/category-api.service";
import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { CategoryEntity } from "./entity/category.entity";
import { UserEntity } from "src/user/entity/user.entity";
import { CategoryApiController } from "src/category-api/category-api.controller";
import { CategoryRepository } from "./category.repository";
import { UserCheckRepository } from "./user.check.repository";
import { GetUserEntityService } from "./get-user-entity.service";
import { AddCategoryService } from "./add-category.service";

@Module({
  imports: [TypeOrmModule.forFeature([CategoryEntity, UserEntity])],
  controllers: [CategoryApiController],
  providers: [CategoryRepository, UserCheckRepository, CategoryApiService, GetUserEntityService, AddCategoryService],
  exports: [GetUserEntityService, AddCategoryService],
})
export class CategoryModule {}
