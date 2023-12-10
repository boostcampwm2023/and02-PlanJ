package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("deviceToken") val deviceToken: String
)

data class SignInNaverRequest(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("deviceToken") val deviceToken: String
)
