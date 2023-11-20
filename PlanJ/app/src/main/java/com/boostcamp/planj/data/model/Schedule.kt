package com.boostcamp.planj.data.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey val scheduleId: String,
    val title: String,
    val memo: String? = null,
    val startTime: String? = null,
    val endTime: String,
    val categoryTitle: String,
    val repeat: Repetition? = null,
    val members: List<User>,
    val doneMembers: List<User>? = null,
    val location: String? = null,
    val finished: Boolean,
    val failed: Boolean
)
