package com.boostcamp.planj.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val scheduleId: String,
    val title: String,
    val startAt: DateTime? = null,
    val endAt: DateTime,
    val isFinished: Boolean = false,
    val isFailed: Boolean = false,
    val hasRetrospectiveMemo: Boolean= false,
    val shared: Boolean = false,
    val repeated: Boolean = false,
    val participantCount: Int = 1,
    val participantSuccessCount: Int = 1,
    val startLocation: Location? = null,
    val endLocation: Location? = null,
    val repetition: Repetition? = null,
    val description: String? = null,
    val participants: List<Participant> = emptyList(),
    val alarm: Alarm? = null,
    val categoryName: String = "미분류",
) : Parcelable