import { Inject, Injectable, InternalServerErrorException } from "@nestjs/common";
import { ConfigType } from "@nestjs/config";
import awsConfig from "../config/aws.config";
import * as AWS from "aws-sdk";
import { ulid } from "ulid";

@Injectable()
export class ImageService {
  private s3: AWS.S3;

  constructor(@Inject(awsConfig.KEY) private config: ConfigType<typeof awsConfig>) {
    this.s3 = new AWS.S3({
      endpoint: new AWS.Endpoint(config.endPoint as string),
      region: config.region as string,
      credentials: {
        accessKeyId: config.accessKey as string,
        secretAccessKey: config.secretKey as string,
      },
    });
  }

  async uploadImage(profileImage: Express.Multer.File) {
    const { buffer, originalname } = profileImage;
    const date = new Date();
    const [, extension] = originalname.split(".");
    const fileName = ulid();
    const directoryPath = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}/`;
    const filePath = directoryPath + `${fileName}.${extension}`;

    try {
      await this.s3
        .putObject({
          Bucket: this.config.bucket as string,
          Key: filePath,
          Body: buffer,
          ACL: "public-read",
        })
        .promise();
      return this.getProfileImageUrl(filePath);
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  private getProfileImageUrl(filePath: string) {
    return this.config.endPoint + "/" + this.config.bucket + "/" + filePath;
  }
}
