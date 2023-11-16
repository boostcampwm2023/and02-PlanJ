package com.boostcamp.planj.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val selectedCategory = MutableStateFlow("")
    val scheduleTitle = MutableStateFlow("")
    val startTime = MutableStateFlow("설정 안함")
    val alarmInfo = MutableStateFlow("")
    val repeatInfo = MutableStateFlow("설정 안함")
    val locationInfo = MutableStateFlow<String?>(null)
    val userMemo = MutableStateFlow<String?>(null)

    val categoryList: StateFlow<List<String>> =
        mainRepository.getCategories()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    fun saveSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            val newSchedule = Schedule(
                scheduleId = scheduleTitle.value,
                title = scheduleTitle.value,
                memo = userMemo.value,
                startTime = "2023-11-14T17:00:11",
                endTime = "2023-11-16T18:00:11",
                categoryTitle = selectedCategory.value,
                repeat = null,
                members = listOf(),
                doneMembers = null,
                location = locationInfo.value,
                finished = false,
                failed = false
            )
            mainRepository.insertSchedule(newSchedule)
            _isComplete.value = true
        }
    }

}