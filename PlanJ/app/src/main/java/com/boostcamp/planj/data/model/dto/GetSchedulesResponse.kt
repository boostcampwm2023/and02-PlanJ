package com.boostcamp.planj.data.model.dto

import com.boostcamp.planj.data.model.ScheduleInfo
import com.google.gson.annotations.SerializedName

data class GetSchedulesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<ScheduleInfo>
)
