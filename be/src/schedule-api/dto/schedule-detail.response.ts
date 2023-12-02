import { ParticipantResponse } from "./participant.response";
import { AlarmInfoResponse } from "./alarmInfo.response";
import { RepetitionResponse } from "./repetition.response";
import { LocationResponse } from "./location.response";

export interface ScheduleDetailResponse {
  categoryName: string;
  scheduleUuid: string;
  title: string;
  description: string;
  startAt: string;
  endAt: string;
  startLocation: LocationResponse;
  endLocation: LocationResponse;
  repetition: RepetitionResponse;
  participants: ParticipantResponse[];
  alarm: AlarmInfoResponse;
}
