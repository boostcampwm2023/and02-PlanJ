import { Test, TestingModule } from "@nestjs/testing";
import { CategoryApiController } from "./category-api.controller";

describe("CategoryApiController", () => {
  let controller: CategoryApiController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [CategoryApiController],
    }).compile();

    controller = module.get<CategoryApiController>(CategoryApiController);
  });

  it("should be defined", () => {
    expect(controller).toBeDefined();
  });
});
