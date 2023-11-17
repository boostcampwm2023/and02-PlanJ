package com.boostcamp.planj.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.MainRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val mainRepository: MainRepositoryImpl,
) : ViewModel() {

    val schedules : StateFlow<List<Schedule>> =
        mainRepository.getSchedules()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun insertSchedule(schedule: Schedule){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertSchedule(schedule)
        }
    }

    fun deleteSchedule(position : Int) : Schedule{
        val schedule = schedules.value[position]
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteSchedule(schedule)
        }
        return schedule
    }

    fun saveSchedule(position : Int){
        val schedule = schedules.value[position]
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertSchedule(schedule)
        }
    }
}