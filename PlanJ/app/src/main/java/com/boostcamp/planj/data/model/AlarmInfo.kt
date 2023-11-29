package com.boostcamp.planj.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmInfo(
    @PrimaryKey val scheduleId: String,
    val alarmTimeInMilliseconds: Long
)
