package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class CategoryData(val categoryUuid : String)

data class PostCategoryResponse(
    @SerializedName("message") val message : String,
    @SerializedName("data") val categoryData : CategoryData,
)