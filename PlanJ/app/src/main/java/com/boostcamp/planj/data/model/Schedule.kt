package com.boostcamp.planj.data.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey val scheduleId : String,
    val title : String,
    val memo : String?,
    val startTime : String?,
    val endTime : String,
    val categoryTitle : String,
    val repeat : Repetition?,
    val members : List<User>,
    val doneMembers : List<User>?,
    val location : String?,
    val finished : Boolean,
    val failed : Boolean
)
