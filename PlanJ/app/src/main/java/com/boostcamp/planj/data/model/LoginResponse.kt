package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: String
)