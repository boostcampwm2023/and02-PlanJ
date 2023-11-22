package com.boostcamp.planj.data.model


import com.google.gson.annotations.SerializedName

data class SameName(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("region") val region: List<String>,
    @SerializedName("selected_region") val selectedRegion: String
)