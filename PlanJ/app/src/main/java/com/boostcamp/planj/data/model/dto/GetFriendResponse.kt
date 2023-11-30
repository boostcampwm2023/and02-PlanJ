package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class FriendInfo(
    @SerializedName("profileUrl") val profileUrl: String? = null,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String
)

data class GetFriendResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<FriendInfo>
)