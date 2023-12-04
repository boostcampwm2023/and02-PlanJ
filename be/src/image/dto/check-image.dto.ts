export interface CheckImageDto {
  version: string;
  requestId: string;
  timestamp: number;
  images: [
    {
      name: string;
      data: string;
    },
  ];
}
