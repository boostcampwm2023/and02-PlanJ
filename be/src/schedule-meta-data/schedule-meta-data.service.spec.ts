import { Test, TestingModule } from "@nestjs/testing";
import { ScheduleMetaDataService } from "./schedule-meta-data.service";

describe("ScheduleMetaDataService", () => {
  let service: ScheduleMetaDataService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [ScheduleMetaDataService],
    }).compile();

    service = module.get<ScheduleMetaDataService>(ScheduleMetaDataService);
  });

  it("should be defined", () => {
    expect(service).toBeDefined();
  });
});
