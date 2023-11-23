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
    val startTime: String? = null,
    val endTime: String,
    val categoryTitle: String,
    val repetition: Repetition? = null,
    val alarm: Alarm? = null,
    val members: List<User>,
    val doneMembers: List<User>? = null,
    val location: String? = null,
    val finished: Boolean,
    val failed: Boolean,

    ) : Parcelable
