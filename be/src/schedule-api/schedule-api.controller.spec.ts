import { Test, TestingModule } from "@nestjs/testing";
import { ScheduleApiController } from "./schedule-api.controller";

describe("ScheduleApiController", () => {
  let controller: ScheduleApiController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [ScheduleApiController],
    }).compile();

    controller = module.get<ScheduleApiController>(ScheduleApiController);
  });

  it("should be defined", () => {
    expect(controller).toBeDefined();
  });
});
