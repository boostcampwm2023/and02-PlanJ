package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

// TODO : 친구 추가 요청 Body 확정되면 변경하기
data class PostFriendRequest(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String
)