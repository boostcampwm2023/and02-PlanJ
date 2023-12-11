package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class Uid(val token : String)

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: String,
    @SerializedName("data") val uid : Uid
)