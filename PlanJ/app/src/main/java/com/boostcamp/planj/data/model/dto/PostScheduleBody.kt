package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class PostScheduleBody(
    @SerializedName("categoryUuid") val categoryUuid : String,
    @SerializedName("title") val title : String,
    @SerializedName("endAt") val endAt : String
)
