import { Request, Response, NextFunction } from "express";
import { GlobalService } from "./disable-connection.global";

export function setConnectionHeader(req: Request, res: Response, next: NextFunction) {
  if (GlobalService.isDisableConnection) {
    res.set("Connection", "close");
  }
  next();
}
