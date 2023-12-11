import { createParamDecorator, ExecutionContext } from "@nestjs/common";

export const Token = createParamDecorator((data: string, context: ExecutionContext) => {
  const request = context.switchToHttp().getRequest();
  const token = request.headers.authorization as string;
  return token.replace(/^Bearer /i, "");
});
