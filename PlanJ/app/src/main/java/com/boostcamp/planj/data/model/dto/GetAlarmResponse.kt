package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class ScheduleAlarm(
    @SerializedName("title") val title: String ="",
    @SerializedName("endAt") val endAt: String="",
    @SerializedName("scheduleUuid") val scheduleUuid: String="",
    @SerializedName("alarmTime") val alarmTime: Int=0,
    @SerializedName("alarmType") val alarmType: String="",
    @SerializedName("estimatedTime") val estimatedTime: Int = 0,
)

data class GetAlarmResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<ScheduleAlarm>
)