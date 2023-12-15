package com.boostcamp.planj.data

import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.ScheduleInfo

fun scheduleReformat(scheduleInfo: ScheduleInfo) : Schedule {
    with(scheduleInfo){
        val startAt =
            startAt?.split("T", "-", ":")?.map { time -> time.toInt() }
                ?: emptyList()
        val endAt = endAt.split("T", "-", ":").map { time -> time.toInt() }
        return Schedule(
            scheduleId = scheduleUuid,
            title = title,
            startAt = if (startAt.isEmpty()) null else DateTime(
                startAt[0],
                startAt[1],
                startAt[2],
                startAt[3],
                startAt[4],
                startAt[5]
            ),
            endAt = DateTime(
                endAt[0],
                endAt[1],
                endAt[2],
                endAt[3],
                endAt[4],
                endAt[5]
            ),
            isFinished = isFinished,
            isFailed = isFailed,
            repeated = repeated,
            hasRetrospectiveMemo = hasRetrospectiveMemo,
            shared = shared,
            participantCount = participantCount,
            participantSuccessCount = participantSuccessCount,
            isAuthor = isAuthor
        )
    }
}

fun getDateTime(scheduleTime: String?): DateTime? {
    val at = scheduleTime?.split("T", "-", ":")?.map { time -> time.toInt() }
    return at?.let {
        DateTime(it[0], it[1], it[2], it[3], it[4])
    }
}