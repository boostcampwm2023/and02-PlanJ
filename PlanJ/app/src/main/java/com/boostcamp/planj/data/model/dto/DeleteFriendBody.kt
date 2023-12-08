package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class DeleteFriendBody(
    @SerializedName("email") val email: String
)
