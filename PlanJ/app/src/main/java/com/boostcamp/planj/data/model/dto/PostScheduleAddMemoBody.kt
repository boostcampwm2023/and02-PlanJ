package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class PostScheduleAddMemoBody(
    @SerializedName("scheduleUuid") val scheduleUuid : String,
    @SerializedName("retrospectiveMemo") val retrospectiveMemo : String

)
