package com.boostcamp.planj.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmInfo(
    @PrimaryKey val scheduleId: String,
    val title: String,
    val endTime: DateTime,
    val repetition: Repetition?,
    val alarm: Alarm
)
