package com.boostcamp.planj.data.model.naver


import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("trafast") val trafast: List<Trafast>
)