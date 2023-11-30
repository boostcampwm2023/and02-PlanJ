package com.boostcamp.planj.data.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repetition(
    @SerializedName("cycleType") val cycleType: String, // daily or weekly
    @SerializedName("cycleCount") val cycleCount: Int
) : Parcelable