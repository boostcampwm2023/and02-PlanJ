package com.boostcamp.planj.data.model

import androidx.room.Entity

@Entity
data class AlarmInfo(
    val scheduleId: String,
    val alarmTimeInMilliseconds: Long
)
