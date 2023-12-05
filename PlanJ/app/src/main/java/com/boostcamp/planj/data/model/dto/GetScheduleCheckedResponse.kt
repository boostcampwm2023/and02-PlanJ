package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName


data class CheckedData(
    @SerializedName("failed") val failed:Boolean,
    @SerializedName("hasRetrospectiveMemo") val hasRetrospectiveMemo:Boolean
)
data class GetScheduleCheckedResponse(
    @SerializedName("message") val message:String,
    @SerializedName("data") val data : CheckedData
)
