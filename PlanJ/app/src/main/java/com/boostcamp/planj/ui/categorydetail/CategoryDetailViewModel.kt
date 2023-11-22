package com.boostcamp.planj.ui.categorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedule: StateFlow<List<Schedule>> = _schedules.asStateFlow()

    fun getCategoryTitleSchedule(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _schedules.value = mainRepository.getCategoryTitleSchedule(title)
        }
    }

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteSchedule(schedule)
            getCategoryTitleSchedule(_title.value)
        }
    }

    fun insertSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertSchedule(schedule)
            getCategoryTitleSchedule(_title.value)
        }
    }

    suspend fun getAllSchedule() = withContext(Dispatchers.IO) {
        _schedules.value = mainRepository.getSchedules().first()
    }

    fun setTitle(title: String) {
        _title.value = title
    }

}