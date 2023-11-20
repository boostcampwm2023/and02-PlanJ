import { Test, TestingModule } from "@nestjs/testing";
import { AuthApiController } from "./auth-api.controller";

describe("AuthApiController", () => {
  let controller: AuthApiController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [AuthApiController],
    }).compile();

    controller = module.get<AuthApiController>(AuthApiController);
  });

  it("should be defined", () => {
    expect(controller).toBeDefined();
  });
});
