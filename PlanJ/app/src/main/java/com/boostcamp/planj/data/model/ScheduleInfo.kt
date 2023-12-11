package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class ScheduleInfo(
    @SerializedName("scheduleUuid") val scheduleUuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("startAt") val startAt: String? = null,
    @SerializedName("endAt") val endAt: String,
    @SerializedName("finished") val isFinished: Boolean = false,
    @SerializedName("failed") val isFailed: Boolean = false,
    @SerializedName("repeated") val repeated : Boolean = false,
    @SerializedName("hasRetrospectiveMemo") val hasRetrospectiveMemo : Boolean,
    @SerializedName("shared") val shared: Boolean,
    @SerializedName("participantCount") val participantCount: Int,
    @SerializedName("participantSuccessCount") val participantSuccessCount: Int,
)