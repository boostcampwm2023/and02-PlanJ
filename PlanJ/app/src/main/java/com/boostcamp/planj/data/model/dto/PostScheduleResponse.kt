package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class ScheduleData(val scheduleUuid : String)

data class PostScheduleResponse(
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : ScheduleData
)
