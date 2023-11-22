package com.boostcamp.planj.data.model


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("same_name") val sameName: SameName,
    @SerializedName("total_count") val totalCount: Int
)