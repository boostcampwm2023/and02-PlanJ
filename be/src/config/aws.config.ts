import { registerAs } from "@nestjs/config";

export default registerAs("aws", () => ({
  accessKey: process.env.AWS_ACCESS_KEY,
  secretKey: process.env.AWS_SECRET_KEY,
  region: process.env.AWS_REGION,
  endPoint: process.env.S3_END_POINT,
  bucket: process.env.S3_BUCKET,
  acl: process.env.S3_ACL,
}));
