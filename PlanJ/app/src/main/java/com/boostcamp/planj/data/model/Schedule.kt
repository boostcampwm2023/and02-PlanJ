package com.boostcamp.planj.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey val scheduleId: String,
    val title: String,
    val memo: String? = null,
    val startTime: DateTime? = null,
    val endTime: DateTime,
    val categoryTitle: String,
    val repetition: Repetition? = null,
    val alarm: Alarm? = null,
    val members: List<User> = listOf(),
    val doneMembers: List<User>? = null,
    val location: Location? = null,
    val startLocation : Location? = null,
    val isFinished: Boolean = false,
    val isFailed: Boolean = false,
    val remindMemo: String = "",
) : Parcelable
