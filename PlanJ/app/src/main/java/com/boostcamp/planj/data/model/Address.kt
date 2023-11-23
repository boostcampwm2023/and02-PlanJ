package com.boostcamp.planj.data.model


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("place_name") val placeName: String,
    @SerializedName("place_url") val placeUrl: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    @SerializedName("x") val x: String,
    @SerializedName("y") val y: String
)