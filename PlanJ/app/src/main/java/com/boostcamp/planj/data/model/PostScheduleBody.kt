package com.boostcamp.planj.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostScheduleBody(
    @SerializedName("categoryUuid") val categoryUuid : String,
    @SerializedName("title") val title : String,
    @SerializedName("endAt") val endAt : String,
    @SerializedName("startLocation") val startLocation: TempLocation= TempLocation(),
    @SerializedName("endLocation") val endLocation : TempLocation=TempLocation()
)

@Parcelize
data class TempLocation(
    val placeName : String="",
    val address : String="",
    val latitude : Double=23.123,
    val longitude : Double=23.123
) : Parcelable

