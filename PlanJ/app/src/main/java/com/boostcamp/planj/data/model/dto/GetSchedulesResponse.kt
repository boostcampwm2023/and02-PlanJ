package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class ScheduleInfo(
    @SerializedName("scheduleUuid") val scheduleUuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("startAt") val startAt: String? = null,
    @SerializedName("endAt") val endAt: String,
    @SerializedName("finished") val isFinished: Boolean = false,
    @SerializedName("failed") val isFailed: Boolean = false,
    @SerializedName("remindMemo") val remindMemo: String
)

data class GetSchedulesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val date: List<ScheduleInfo>
)
