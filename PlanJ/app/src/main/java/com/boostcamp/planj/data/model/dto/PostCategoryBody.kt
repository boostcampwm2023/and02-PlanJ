package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class PostCategoryBody(
    @SerializedName("categoryName") val categoryName : String,
)
