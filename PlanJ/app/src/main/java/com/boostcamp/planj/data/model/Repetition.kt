package com.boostcamp.planj.data.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repetition(
    @SerializedName("repetition_id") val repetitionId: String,// auto_increment
    @SerializedName("cycle_type") val cycleType: String, // daily or weekly
    @SerializedName("cycle_count") val cycleCount: Int
) : Parcelable