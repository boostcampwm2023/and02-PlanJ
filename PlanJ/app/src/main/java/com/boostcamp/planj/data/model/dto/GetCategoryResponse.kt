package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class GetCategoryResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<PatchCategoryRequest>
)