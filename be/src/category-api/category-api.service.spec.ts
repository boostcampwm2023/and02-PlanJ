import { Test, TestingModule } from "@nestjs/testing";
import { CategoryApiService } from "./category-api.service";

describe("CategoryApiService", () => {
  let service: CategoryApiService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [CategoryApiService],
    }).compile();

    service = module.get<CategoryApiService>(CategoryApiService);
  });

  it("should be defined", () => {
    expect(service).toBeDefined();
  });
});
