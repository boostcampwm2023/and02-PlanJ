package com.boostcamp.planj.data.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostScheduleBody(
    @SerializedName("categoryUuid") val categoryUuid : String,
    @SerializedName("title") val title : String,
    @SerializedName("endAt") val endAt : String,
)