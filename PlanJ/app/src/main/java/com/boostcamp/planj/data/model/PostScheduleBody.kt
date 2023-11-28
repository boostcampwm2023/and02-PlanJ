package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class PostScheduleBody(
    @SerializedName("categoryUuid") val categoryUuid : String,
    @SerializedName("title") val title : String,
    @SerializedName("endAt") val endAt : String
)
