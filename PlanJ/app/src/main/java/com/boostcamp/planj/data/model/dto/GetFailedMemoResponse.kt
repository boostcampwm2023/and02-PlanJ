package com.boostcamp.planj.data.model.dto

import com.boostcamp.planj.data.model.FailedMemo
import com.google.gson.annotations.SerializedName

data class GetFailedMemoResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val date: List<FailedMemo>
)
