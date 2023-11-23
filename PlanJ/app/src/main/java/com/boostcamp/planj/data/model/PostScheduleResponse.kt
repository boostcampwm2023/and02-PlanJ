package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class ScheduleData(val scheduleUuid : String)

data class PostScheduleResponse(
    @SerializedName("message") val message : String,
    @SerializedName("statusCode") val statusCode : String,
    @SerializedName("data") val data : ScheduleData
)
