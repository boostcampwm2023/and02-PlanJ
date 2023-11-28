package com.boostcamp.planj.ui.main.home.week

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.WeekSchedule
import com.boostcamp.planj.data.repository.MainRepositoryImpl
import com.boostcamp.planj.ui.main.home.week.adapter.CalendarVO
import com.boostcamp.planj.ui.main.home.week.adapter.ScheduleType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class WeekFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepositoryImpl,
) : ViewModel() {

    val scheduleList =
        mainRepository.getWeekSchedule()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(50000), emptyList())
    lateinit var calendarList: ArrayList<CalendarVO>
    lateinit var today: String


    fun makeWeekSchedule(calendarList: ArrayList<CalendarVO>): ArrayList<WeekSchedule> {
        val array: ArrayList<WeekSchedule> = arrayListOf()



        calendarList.forEach { calendarVO ->
            val haveStart = scheduleList.value.filter {
                it.startTime != null && it.startTime.split("[-:T]".toRegex())[2].toInt() == calendarVO.dayNumber.toInt()
                        && it.startTime.split("[-:T]".toRegex())[2] != it.endTime.split("[-:T]".toRegex())[2]
            }

            val emptyStart: List<Schedule> = scheduleList.value.filter {
                (it.startTime == null
                        && it.endTime.split("[-:T]".toRegex())[2].toInt() >= calendarVO.dayNumber.toInt()
                        && today == calendarVO.dayNumber)
                        ||
                        (it.startTime != null
                                && it.startTime.split("[-:T]".toRegex())[2] == it.endTime.split("[-:T]".toRegex())[2]
                                && it.endTime.split("[-:T]".toRegex())[2] == calendarVO.dayNumber)
            }

            val endList = scheduleList.value.filter {
                it.endTime.split("[-:T]".toRegex())[2] == calendarVO.dayNumber
                        && calendarVO.dayNumber != today
                        && ((it.startTime?.split("[-:T]".toRegex())?.get(2) ?: "") != it.endTime.split("[-:T]".toRegex())[2])
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