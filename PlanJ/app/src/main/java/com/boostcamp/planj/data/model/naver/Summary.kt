package com.boostcamp.planj.data.model.naver


import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("bbox") val bbox: List<List<Double>>,
    @SerializedName("departureTime") val departureTime: String,
    @SerializedName("distance") val distance: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("etaServiceType") val etaServiceType: Int,
    @SerializedName("fuelPrice") val fuelPrice: Int,
    @SerializedName("goal") val goal: Goal,
    @SerializedName("start") val start: Start,
    @SerializedName("taxiFare") val taxiFare: Int,
    @SerializedName("tollFare") val tollFare: Int
)