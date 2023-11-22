package com.boostcamp.planj.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
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

    private val _members = MutableStateFlow(listOf(User("1111"), User("2222"), User("3333"), User("4444")))
    val members: StateFlow<List<User>> = _members

    private val _doneMembers = MutableStateFlow<List<User>?>(null)
    val doneMembers: StateFlow<List<User>?> = _doneMembers

    private val _scheduleAlarm = MutableStateFlow<String?>(null)
    val scheduleAlarm: StateFlow<String?> = _scheduleAlarm

    private val _scheduleRepetition = MutableStateFlow<Repetition?>(Repetition("aaa", "daily", 4))
    val scheduleRepetition: StateFlow<Repetition?> = _scheduleRepetition
    val locationInfo = MutableStateFlow<Location?>(null)
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

    fun setRepetition(repetition: Repetition?) {
        _scheduleRepetition.value = repetition
    }

    fun setAlarm(alarm: String?) {
        _scheduleAlarm.value = alarm
    }

    fun setLocation(location: Location?){
        locationInfo.value = location
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
                repetition = null,
                members = listOf(),
                doneMembers = null,
                location = locationInfo.value?.placeName,
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