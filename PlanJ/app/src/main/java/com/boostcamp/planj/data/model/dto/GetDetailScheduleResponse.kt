package com.boostcamp.planj.data.model.dto

import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.data.model.Repetition
import com.google.gson.annotations.SerializedName

data class GetDetailScheduleResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val scheduleDetail: ScheduleDetail
)

data class ScheduleDetail(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("scheduleUuid") val scheduleUuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("startAt") val startAt: String?,
    @SerializedName("endAt") val endAt: String,
    @SerializedName("startLocation") val startLocation: Location?,
    @SerializedName("endLocation") val endLocation: Location?,
    @SerializedName("repetition") val repetition: Repetition?,
    @SerializedName("participants") val participants: List<Participant> = emptyList(),
    @SerializedName("alarm") val alarm: Alarm?
)