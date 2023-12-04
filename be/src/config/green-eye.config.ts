import { registerAs } from "@nestjs/config";

export default registerAs("greenEye", () => ({
  version: process.env.GREENEYE_VERSION,
  url: process.env.GREENEYE_URL,
  secret: process.env.GREENEYE_SECRET,
  adultThreshold: parseFloat(process.env.GREENEYE_ADULT_THRESHOLD),
  pornThreshold: parseFloat(process.env.GREENEYE_PORN_THRESHOLD),
}));
