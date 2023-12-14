export interface PayloadDto {
  token: string;
  data: {
    title: string;
    body: string;
    index?: string;
  };
}
