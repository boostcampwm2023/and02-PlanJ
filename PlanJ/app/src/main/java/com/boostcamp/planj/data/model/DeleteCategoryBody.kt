package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class DeleteCategoryBody(
    @SerializedName("userUuid") val userUuid : String,
    @SerializedName("categoryUuid") val categoryUuid : String
)