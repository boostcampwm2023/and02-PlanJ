package com.boostcamp.planj.ui.main.home.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object Error : UiState()
}*/

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val mainRepository: MainRepositoryImpl,
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
            mainRepository.deleteSchedule(schedule)
        }
    }
}