package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class PatchScheduleData(
    @SerializedName("scheduleUuid") val scheduleUuid: String
)

data class PatchScheduleResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PatchScheduleData,
)