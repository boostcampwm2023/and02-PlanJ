package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class PatchScheduleData(val scheduleUuid : String)

data class PatchScheduleResponse(
    @SerializedName("message") val message : String,
    @SerializedName("statusCode") val statusCode : String,
    @SerializedName("data") val data : PatchScheduleData,

    )
