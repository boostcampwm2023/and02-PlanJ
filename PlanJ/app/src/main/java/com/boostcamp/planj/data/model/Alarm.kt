package com.boostcamp.planj.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Alarm(
    val alarmType: String,
    val alarmTime: Int,
    val estimatedTime: Int = 0
) : Parcelable
