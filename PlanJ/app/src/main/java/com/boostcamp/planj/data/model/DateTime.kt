package com.boostcamp.planj.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class DateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int = 0
) : Parcelable {
    fun toFormattedString(): String {
        return String.format(
            "%04d-%02d-%02dT%02d:%02d:%02d",
            year,
            month,
            day,
            hour,
            minute,
            second
        )
    }

    fun toMilliseconds(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute, second)
        return calendar.timeInMillis
    }
}