package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class PatchCategoryRequest(
    @SerializedName("categoryUuid") val categoryUuid: String,
    @SerializedName("categoryName") val categoryName: String
)