import { registerAs } from "@nestjs/config";

export default registerAs("auth", () => ({
  options: {
    expiresIn: process.env.JWT_OPTION_EXPIRES_IN,
    issuer: process.env.JWT_ISSUER,
  },
  jwtSecret: process.env.JWT_SECRET,
}));
