package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class DeleteScheduleBody(
    @SerializedName("userUuid") val userUuid : String,
    @SerializedName("scheduleUuid") val scheduleUuid : String
)
