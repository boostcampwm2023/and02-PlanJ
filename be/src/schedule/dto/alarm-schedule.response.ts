import { AlarmInfoResponse } from "../../schedule-api/dto/alarmInfo.response";

export interface AlarmScheduleResponse {
  title: string;
  endAt: string;
  scheduleUuid: string;
  alarm: AlarmInfoResponse;
}
