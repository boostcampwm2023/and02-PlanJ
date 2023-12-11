package com.boostcamp.planj.data.model.dto

import com.boostcamp.planj.data.model.User
import com.google.gson.annotations.SerializedName

data class GetFriendResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<User>
)