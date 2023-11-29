package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class PostUserBody(
    @SerializedName("nickname") val nickname : String
)

data class PostUserResponse(
    @SerializedName("message") val message : String
)
