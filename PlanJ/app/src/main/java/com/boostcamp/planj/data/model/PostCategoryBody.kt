package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class PostCategoryBody(
    @SerializedName("userUuid") val userUuid : String,
    @SerializedName("categoryName") val categoryName : String,
    @SerializedName("createdAt") val createdAt: String
)
