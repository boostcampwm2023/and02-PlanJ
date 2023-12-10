package com.boostcamp.planj.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmInfo(
    @PrimaryKey val scheduleId: String,
    val title: String,
    val endTime: DateTime,
    val alarmType: String,
    val alarmTime: Int,
    val estimatedTime: Int = 0
)