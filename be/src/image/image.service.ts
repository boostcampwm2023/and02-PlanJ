import { Inject, Injectable, InternalServerErrorException, Logger, UnprocessableEntityException } from "@nestjs/common";
import { ConfigType } from "@nestjs/config";
import awsConfig from "../config/aws.config";
import * as AWS from "aws-sdk";
import { ulid } from "ulid";
import axios from "axios";
import { CheckImageDto } from "./dto/check-image.dto";
import greenEyeConfig from "../config/green-eye.config";

@Injectable()
export class ImageService {
  private s3: AWS.S3;
  private readonly logger = new Logger(ImageService.name);

  constructor(
    @Inject(awsConfig.KEY) private s3Config: ConfigType<typeof awsConfig>,
    @Inject(greenEyeConfig.KEY) private imageConfig: ConfigType<typeof greenEyeConfig>,
  ) {
    this.s3 = new AWS.S3({
      endpoint: new AWS.Endpoint(s3Config.endPoint),
      region: s3Config.region,
      credentials: {
        accessKeyId: s3Config.accessKey,
        secretAccessKey: s3Config.secretKey,
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
    await this.checkValidateImage(profileImage);

    try {
      await this.s3
        .putObject({
          Bucket: this.s3Config.bucket,
          Key: filePath,
          Body: buffer,
          ACL: this.s3Config.acl,
        })
        .promise();
      return this.getProfileImageUrl(filePath);
    } catch (e) {
      throw new InternalServerErrorException();
    }
  }

  private async checkValidateImage(profileImage: Express.Multer.File) {
    const buffer = profileImage.buffer;
    const base64String = buffer.toString("base64");
    const data: CheckImageDto = {
      version: this.imageConfig.version,
      requestId: ulid(),
      timestamp: Date.now(),
      images: [
        {
          name: profileImage.originalname,
          data: base64String,
        },
      ],
    };

    try {
      const response = await axios.post(this.imageConfig.url, data, {
        headers: {
          "Content-Type": "application/json",
          "X-GREEN-EYE-SECRET": this.imageConfig.secret,
        },
      });

      const [images] = response.data.images;
      const adultConfidence = images.result.adult.confidence as number;
      const pornConfidence = images.result.porn.confidence as number;

      if (adultConfidence > this.imageConfig.adultThreshold || pornConfidence > this.imageConfig.pornThreshold) {
        throw new UnprocessableEntityException("서비스 규칙에 위배되는 사진입니다.");
      }
      return;
    } catch (e) {
      this.logger.error(e);
      throw e;
    }
  }

  private getProfileImageUrl(filePath: string) {
    return this.s3Config.endPoint + "/" + this.s3Config.bucket + "/" + filePath;
  }
}
