package com.boostcamp.planj.ui.main.home.week

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.WeekSchedule
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.ui.main.home.week.adapter.CalendarVO
import com.boostcamp.planj.ui.main.home.week.adapter.ScheduleType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class WeekFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val scheduleList =
        mainRepository.getSchedules()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(50000), emptyList())
    lateinit var calendarList: ArrayList<CalendarVO>
    lateinit var today: String

    fun makeWeekSchedule(calendarList: ArrayList<CalendarVO>): ArrayList<WeekSchedule> {
        val array: ArrayList<WeekSchedule> = arrayListOf()

        calendarList.forEach { calendarVO ->
            val haveStart = scheduleList.value.filter {
                it.startTime != null && it.startTime.day == calendarVO.dayNumber.toInt()
                        && it.startTime.day != it.endTime.day
            }

            val emptyStart: List<Schedule> = scheduleList.value.filter {
                (it.startTime == null && it.endTime.day == calendarVO.dayNumber.toInt())
                        || (it.startTime != null && it.startTime.day == it.endTime.day && it.endTime.day == calendarVO.dayNumber.toInt())
            }

            val endList = scheduleList.value.filter {
                it.endTime.day == calendarVO.dayNumber.toInt()
                        && calendarVO.dayNumber != today
                        && it.startTime != null
                        && (it.startTime.day != it.endTime.day)
            }

            val totalList: MutableList<ScheduleType> = mutableListOf()

            haveStart.forEach { schedule: Schedule ->
                totalList.add(ScheduleType(schedule, 0))
            }
            endList.forEach { schedule: Schedule ->
                totalList.add(ScheduleType(schedule, 2))
            }
            emptyStart.forEach { schedule: Schedule ->
                totalList.add(ScheduleType(schedule, 1))
            }
            array.add(WeekSchedule(calendarVO, totalList))
        }
        return array
    }
}