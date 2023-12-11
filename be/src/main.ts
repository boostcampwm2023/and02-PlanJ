import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";
import { ValidationPipe } from "@nestjs/common";
import { DocumentBuilder, SwaggerModule } from "@nestjs/swagger";

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(
    new ValidationPipe({
      transform: true,
    }),
  );

  app.enableShutdownHooks();

  //swagger
  const config = new DocumentBuilder()
    .setTitle("PlanJ API")
    .setDescription("API Docs for PlanJ Service")
    .setVersion("1.0")
    .addTag("PlanJ")
    .addBearerAuth({ type: "http", scheme: "bearer", bearerFormat: "JWT" }, "access-token")
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup("api", app, document);

  await app.listen(3000, () => {
    process.send("ready");
  });
}
bootstrap();
