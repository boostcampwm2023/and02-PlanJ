package com.boostcamp.planj.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.data.model.Alarm
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

    private lateinit var scheduleId: String
    private val isFinished = MutableStateFlow(false)
    private val isFailed = MutableStateFlow(false)

    val scheduleCategory = MutableStateFlow("")
    val selectedCategory = scheduleCategory.value
    val scheduleTitle = MutableStateFlow("")

    private val _scheduleStartDate = MutableStateFlow<String?>(null)
    val scheduleStartDate: StateFlow<String?> = _scheduleStartDate
    private val _scheduleStartTime = MutableStateFlow<String?>(null)
    val scheduleStartTime: StateFlow<String?> = _scheduleStartTime

    private val _scheduleEndDate = MutableStateFlow<String?>("")
    val scheduleEndDate: StateFlow<String?> = _scheduleEndDate
    private val _scheduleEndTime = MutableStateFlow<String?>("")
    val scheduleEndTime: StateFlow<String?> = _scheduleEndTime

    private val _members = MutableStateFlow<List<User>>(listOf())
    val members: StateFlow<List<User>> = _members

    private val _doneMembers = MutableStateFlow<List<User>?>(null)
    val doneMembers: StateFlow<List<User>?> = _doneMembers

    private val _scheduleAlarm = MutableStateFlow<Alarm?>(null)
    val scheduleAlarm: StateFlow<Alarm?> = _scheduleAlarm

    private val _scheduleRepetition = MutableStateFlow<Repetition?>(null)
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

    fun setScheduleInfo(schedule: Schedule?) {
        schedule?.let { schedule ->
            scheduleId = schedule.scheduleId
            scheduleCategory.value = schedule.categoryTitle
            scheduleTitle.value = schedule.title

            if (schedule.startTime == null) {
                _scheduleStartDate.value = null
                _scheduleStartTime.value = null
            } else {
                val dateTime = schedule.startTime.split("T")
                _scheduleStartDate.value = dateTime[0].replace("-", "/")
                _scheduleStartTime.value = dateTime[1].split(":").dropLast(1).joinToString(":")
            }

            val dateTime = schedule.endTime.split("T")
            _scheduleEndDate.value = dateTime[0].replace("-", "/")
            _scheduleEndTime.value = dateTime[1].split(":").dropLast(1).joinToString(":")

            _scheduleRepetition.value = schedule.repetition
            _scheduleAlarm.value = schedule.alarm
            _doneMembers.value = schedule.doneMembers
            _members.value = schedule.members
            scheduleLocation.value = schedule.location
            isFinished.value = schedule.isFinished
            isFailed.value = schedule.isFailed
        }
    }

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

    fun setAlarm(alarm: Alarm?) {
        _scheduleAlarm.value = alarm
    }

    fun setLocation(location: Location?){
        locationInfo.value = location
    }

    fun startEditingSchedule() {
        _isEditMode.value = true
    }

    fun deleteSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteScheduleUsingId(scheduleId)
        }
    }

    fun completeEditingSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            val newSchedule = Schedule(
                scheduleId = scheduleId,
                title = scheduleTitle.value,
                memo = scheduleMemo.value,
                startTime = if (scheduleStartDate.value == null) {
                    null
                } else {
                    "${scheduleStartDate.value?.replace('/', '-')}T${scheduleStartTime.value}:00"
                },
                endTime = "${scheduleEndDate.value?.replace('/', '-')}T${scheduleEndTime.value}:00",
                categoryTitle = scheduleCategory.value,
                repetition = scheduleRepetition.value,
                alarm = scheduleAlarm.value,
                members = members.value,
                doneMembers = doneMembers.value,
                location = scheduleLocation.value,
                isFinished = isFinished.value,
                isFailed = isFailed.value
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