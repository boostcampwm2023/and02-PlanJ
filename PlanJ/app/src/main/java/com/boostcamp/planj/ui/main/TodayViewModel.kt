package com.boostcamp.planj.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.MainRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object Error : UiState()
}

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val mainRepository: MainRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Success)
    val uiState = _uiState.asStateFlow()


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