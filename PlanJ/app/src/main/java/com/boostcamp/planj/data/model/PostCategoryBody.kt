package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class PostCategoryBody(
    @SerializedName("categoryName") val categoryName : String,
)
