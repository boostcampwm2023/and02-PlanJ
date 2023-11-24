package com.boostcamp.planj.ui.main.home.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.getDate
import com.boostcamp.planj.getTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/*
sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object Error : UiState()
}*/

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val schedules: StateFlow<List<Schedule>> =
        mainRepository.getSchedules()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun insertSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertSchedule(schedule)
        }
    }

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainRepository.deleteScheduleApi("01HFYAR1FX09FKQ2SW1HTG8BJ8", schedule.scheduleId)
                mainRepository.deleteSchedule(schedule)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "Today delete error ${e.message}")
            }
        }
    }

    fun checkBoxChange(schedule: Schedule, isCheck: Boolean) {
        val calendar = Calendar.getInstance()
        val date = schedule.endTime.getDate().split("-")
        val time = schedule.endTime.getTime().split(":")

        calendar.set(
            date[0].toInt(),
            date[1].toInt() - 1,
            date[2].toInt(),
            time[0].toInt(),
            time[1].toInt(),
            time[2].toInt()
        )
        val fail = calendar.timeInMillis < System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO){
            mainRepository.updateSchedule(schedule.copy(isFailed = fail, isFinished = isCheck))
        }

    }
}