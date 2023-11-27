import { Test, TestingModule } from "@nestjs/testing";
import { UserApiController } from "./user-api.controller";

describe("UserApiController", () => {
  let controller: UserApiController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [UserApiController],
    }).compile();

    controller = module.get<UserApiController>(UserApiController);
  });

  it("should be defined", () => {
    expect(controller).toBeDefined();
  });
});
