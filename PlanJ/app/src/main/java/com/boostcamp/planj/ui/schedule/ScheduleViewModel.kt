package com.boostcamp.planj.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val selectedCategory = MutableStateFlow("미분류")
    val scheduleTitle = MutableStateFlow("")
    val startTime = MutableStateFlow("설정 안함")
    val alarmInfo = MutableStateFlow("")
    val repeatInfo = MutableStateFlow("설정 안함")
    val locationInfo = MutableStateFlow<String?>(null)
    val userMemo = MutableStateFlow<String?>(null)

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    fun saveSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            val newSchedule = Schedule(
                scheduleId = scheduleTitle.value,
                title = scheduleTitle.value,
                memo = userMemo.value,
                startTime = "2023/11/14 17:00",
                endTime = "2023/11/15 18:00",
                categoryTitle = "미분류",
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