package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class DeleteScheduleBody(
    @SerializedName("scheduleUuid") val scheduleUuid: String
)
