package com.boostcamp.planj.data.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repetition(
    val repetitionId: String,// auto_increment
    val cycleType: String, // daily or weekly
    val cycleCountDaily: Int, // 0 ~ 6
    val cycleCountWeekly: Int // 0 ~ 5
) : Parcelable