package com.boostcamp.planj.data.model.naver


import com.google.gson.annotations.SerializedName

data class Start(
    @SerializedName("location") val location: List<Double>
)