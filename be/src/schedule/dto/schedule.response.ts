export interface ScheduleResponse {
  scheduleUuid: string;
  title: string;
  startAt: string;
  endAt: string;
  finished: boolean;
  failed: boolean;
  repeated: boolean;
  shared: boolean;
  participantCount: number;
  participantSuccessCount: number;
}
