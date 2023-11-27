import { createParamDecorator, ExecutionContext } from "@nestjs/common";

export const Token = createParamDecorator((data: string, context: ExecutionContext) => {
  const request = context.switchToHttp().getRequest();
  return request.headers.authorization as string;
});
