package com.boostcamp.planj.data.model

import com.boostcamp.planj.ui.main.home.week.adapter.CalendarVO
import com.boostcamp.planj.ui.main.home.week.adapter.ScheduleType

data class WeekSchedule(
    val calendar: CalendarVO,
    val scheduleList: List<ScheduleType>
)