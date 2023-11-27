export interface ScheduleResponse {
  scheduleUuid: string;
  title: string;
  description: string;
  startAt: string;
  endAt: string;
  finished: boolean;
  failed: boolean;
  remindMemo: string;
}
