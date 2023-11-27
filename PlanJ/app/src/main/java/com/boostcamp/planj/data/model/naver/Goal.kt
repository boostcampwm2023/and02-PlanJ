package com.boostcamp.planj.data.model.naver


import com.google.gson.annotations.SerializedName

data class Goal(
    @SerializedName("dir") val dir: Int,
    @SerializedName("location") val location: List<Double>
)