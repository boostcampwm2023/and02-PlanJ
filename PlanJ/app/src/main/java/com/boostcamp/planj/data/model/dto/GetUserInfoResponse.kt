package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("email") val email: String,
    @SerializedName("profileUrl") val imgUrl: String = ""
)

data class GetUserInfoResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserInfo
)
