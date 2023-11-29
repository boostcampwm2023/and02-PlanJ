package com.boostcamp.planj.data.model.naver


import com.google.gson.annotations.SerializedName

data class Trafast(
    @SerializedName("guide") val guide: List<Guide>,
    @SerializedName("path") val path: List<List<Double>>,
    @SerializedName("section") val section: List<Section>,
    @SerializedName("summary") val summary: Summary
)