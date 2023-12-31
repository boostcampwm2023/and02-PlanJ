package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class SignUpResult(
    @SerializedName("statusCode") val statusCode : String,
    @SerializedName("message") val message : String
)
