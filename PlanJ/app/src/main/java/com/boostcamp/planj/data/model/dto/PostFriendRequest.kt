package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class PostFriendRequest(
    @SerializedName("friendEmail") val friendEmail: String
)