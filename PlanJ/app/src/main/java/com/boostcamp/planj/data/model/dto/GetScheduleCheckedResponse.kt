package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class GetScheduleCheckedResponse(
    @SerializedName("message") val message:String,
    @SerializedName("isFail") val isFail:Boolean,
    @SerializedName("isWrite") val isWrite:Boolean
)
