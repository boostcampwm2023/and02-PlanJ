package com.boostcamp.planj.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Location(
    val placeName : String,
    val address : String,
    val latitude : String,
    val longitude : String
) : Parcelable