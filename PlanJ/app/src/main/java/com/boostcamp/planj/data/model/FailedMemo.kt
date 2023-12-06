package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class FailedMemo(
    @SerializedName("title") val title: String,
    @SerializedName("startAt") val startAt: String?,
    @SerializedName("endAt") val endAt: String,
    @SerializedName("retrospectiveMemo") val retrospectiveMemo: String
)
