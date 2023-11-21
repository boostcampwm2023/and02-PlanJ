package com.boostcamp.planj.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val scheduleCategory = MutableStateFlow("")
    val selectedCategory = scheduleCategory.value
    val scheduleTitle = MutableStateFlow("")

    private val _scheduleStartDate = MutableStateFlow<String?>("2023/11/20")
    val scheduleStartDate: StateFlow<String?> = _scheduleStartDate
    private val _scheduleStartTime = MutableStateFlow<String?>("23:59")
    val scheduleStartTime: StateFlow<String?> = _scheduleStartTime

    private val _scheduleEndDate = MutableStateFlow<String?>("2023/11/20")
    val scheduleEndDate: StateFlow<String?> = _scheduleEndDate
    private val _scheduleEndTime = MutableStateFlow<String?>("23:59")
    val scheduleEndTime: StateFlow<String?> = _scheduleEndTime

    val alarmInfo = MutableStateFlow("")
    val repetitionInfo = MutableStateFlow<Repetition?>(Repetition("aaa", "daily", 4))
    val locationInfo = MutableStateFlow<String?>(null)
    val userMemo = MutableStateFlow<String?>(null)

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd")

    val categoryList: StateFlow<List<String>> =
        mainRepository.getCategories()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    fun getStartDate(): Long? {
        return scheduleStartDate.value?.let { dateFormat.parse(it)?.time }
    }

    fun setStartDate(millis: Long) {
        _scheduleStartDate.value = dateFormat.format(millis)
    }

    fun setStartTime(hour: Int, minute: Int) {
        _scheduleStartTime.value = "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }

    fun getEndDate(): Long? {
        return scheduleEndDate.value?.let { dateFormat.parse(it)?.time }
    }

    fun setEndDate(millis: Long) {
        _scheduleEndDate.value = dateFormat.format(millis)
    }

    fun setEndTime(hour: Int, minute: Int) {
        _scheduleEndTime.value = "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }

    fun startEditingSchedule() {
        _isEditMode.value = true
    }

    fun deleteSchedule() {
        // TODO: 일정 삭제 요청
    }

    fun completeEditingSchedule() {
        // TODO: 일정 수정 요청으로 변경하기
        viewModelScope.launch(Dispatchers.IO) {
            val newSchedule = Schedule(
                scheduleId = scheduleTitle.value,
                title = scheduleTitle.value,
                memo = userMemo.value,
                startTime = "${scheduleStartDate.value}T${scheduleStartTime.value}",
                endTime = "${scheduleEndDate.value}T${scheduleEndTime.value}",
                categoryTitle = scheduleCategory.value,
                repeat = repetitionInfo.value,
                members = listOf(),
                doneMembers = null,
                location = locationInfo.value,
                finished = false,
                failed = false
            )
            mainRepository.insertSchedule(newSchedule)
            _isEditMode.value = false
        }
    }

    fun resetStartTime() {
        _scheduleStartDate.value = null
        _scheduleStartTime.value = null
    }
}