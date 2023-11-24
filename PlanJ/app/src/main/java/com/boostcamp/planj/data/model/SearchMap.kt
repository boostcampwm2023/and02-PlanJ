package com.boostcamp.planj.data.model


import com.google.gson.annotations.SerializedName

data class SearchMap(
    @SerializedName("documents") val addresses: List<Address>,
    @SerializedName("meta") val meta: Meta
)