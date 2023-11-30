package com.boostcamp.planj.data.model.naver


import com.google.gson.annotations.SerializedName

data class NaverResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("currentDateTime") val currentDateTime: String,
    @SerializedName("message") val message: String,
    @SerializedName("route") val route: Route
)