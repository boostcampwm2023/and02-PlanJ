package com.boostcamp.planj.data.model

import com.google.gson.annotations.SerializedName

data class PatchScheduleBody(
    @SerializedName("userUuid") val userUuid : String,
    @SerializedName("categoryUuid") val categoryUuid : String,
    @SerializedName("scheduleUuid") val scheduleUuid : String,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("startAt") val startAt : String,
    @SerializedName("endAt") val endAt : String,
    @SerializedName("placeName") val placeName : String,
    @SerializedName("placeAddress") val placeAddress : String,
    @SerializedName("latitude") val latitude : String,
    @SerializedName("longtitude") val longtitude : String,
)
