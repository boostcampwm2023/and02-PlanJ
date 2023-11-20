import { Test, TestingModule } from "@nestjs/testing";
import { ScheduleApiService } from "./schedule-api.service";

describe("ScheduleApiService", () => {
  let service: ScheduleApiService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [ScheduleApiService],
    }).compile();

    service = module.get<ScheduleApiService>(ScheduleApiService);
  });

  it("should be defined", () => {
    expect(service).toBeDefined();
  });
});
