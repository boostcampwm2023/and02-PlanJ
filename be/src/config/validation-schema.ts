import * as Joi from "joi";

export const validationSchema = Joi.object({
  DB_HOST: Joi.string().required(),
  DB_PORT: Joi.number().required(),
  DB_USERNAME: Joi.string().required(),
  DB_PASSWORD: Joi.string().required(),
  DB_DATABASE: Joi.string().required(),
  JWT_SECRET: Joi.string().required(),
  JWT_ISSUER: Joi.string().required(),
  JWT_OPTION_EXPIRES_IN: Joi.string().required(),
  AWS_ACCESS_KEY: Joi.string().required(),
  AWS_SECRET_KEY: Joi.string().required(),
  AWS_REGION: Joi.string().required(),
  S3_END_POINT: Joi.string().uri().required(),
  S3_BUCKET: Joi.string().required(),
  S3_ACL: Joi.string().required(),
  GREENEYE_VERSION: Joi.string().required(),
  GREENEYE_URL: Joi.string().uri().required(),
  GREENEYE_SECRET: Joi.string().required(),
  GREENEYE_ADULT_THRESHOLD: Joi.number().required(),
  GREENEYE_PORN_THRESHOLD: Joi.number().required(),
});
