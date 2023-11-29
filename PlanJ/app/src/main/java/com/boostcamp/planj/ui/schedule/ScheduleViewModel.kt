package com.boostcamp.planj.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.PatchScheduleBody
import com.boostcamp.planj.data.model.naver.NaverResponse
import com.boostcamp.planj.data.repository.AlarmRepository
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.NaverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val alarmRepository: AlarmRepository,
    private val naverRepository: NaverRepository
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

    // TODO: Schedule members 자료형 바꾸고 doneMembers 제거해야 함
    // 어떤 응답이 올지 몰라 Participant data class 추가하여 구현
    private val _members = MutableStateFlow<List<Participant>>(emptyList())
    val members: StateFlow<List<Participant>> = _members

    private val _doneMembers = MutableStateFlow<List<User>?>(null)
    val doneMembers: StateFlow<List<User>?> = _doneMembers

    private val _scheduleAlarm = MutableStateFlow<Alarm?>(null)
    val scheduleAlarm: StateFlow<Alarm?> = _scheduleAlarm

    private val _scheduleRepetition = MutableStateFlow<Repetition?>(null)
    val scheduleRepetition: StateFlow<Repetition?> = _scheduleRepetition

    private val _endScheduleLocation = MutableStateFlow<Location?>(null)
    val endScheduleLocation = _endScheduleLocation.asStateFlow()
    val scheduleMemo = MutableStateFlow<String?>(null)

    val startScheduleLocation = MutableStateFlow<Location?>(null)

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd")

    val categoryList: StateFlow<List<String>> =
        mainRepository.getCategories()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    private val _route = MutableStateFlow<NaverResponse?>(null)
    val route: StateFlow<NaverResponse?> = _route.asStateFlow()

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
            //_members.value = schedule.members
            _endScheduleLocation.value = schedule.location
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

    fun setLocation(startLocation: Location?, endLocation: Location?) {
        _endScheduleLocation.value = endLocation
        startScheduleLocation.value = startLocation
    }

    fun startEditingSchedule() {
        _isEditMode.value = true
    }

    fun deleteSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainRepository.deleteScheduleApi(scheduleId)
                mainRepository.deleteScheduleUsingId(scheduleId)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "scheduleFragment Delete error ${e.message}")
            }
        }
    }

    fun completeEditingSchedule() {
        viewModelScope.launch {
            if (scheduleAlarm.value != null) {
                val dateList = scheduleEndDate.value?.split("/")!!
                val timeList = scheduleEndTime.value?.split(":")!!
                alarmRepository.setAlarm(
                    AlarmInfo(
                        scheduleId,
                        scheduleTitle.value,
                        DateTime(
                            dateList[0].toInt(),
                            dateList[1].toInt(),
                            dateList[2].toInt(),
                            timeList[0].toInt(),
                            timeList[1].toInt()
                        ),
                        scheduleRepetition.value,
                        scheduleAlarm.value!!
                    )
                )
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val getCategory =
                withContext((Dispatchers.IO)) { mainRepository.getCategory(scheduleCategory.value) }

            val patchScheduleBody = PatchScheduleBody(
                getCategory.categoryId,
                scheduleId,
                scheduleTitle.value,
                scheduleMemo.value,
                scheduleStartDate.value?.let {
                    "${it.replace("/", "-")}T${scheduleStartTime.value}:00"
                },
                scheduleEndDate.value?.let {
                    "${it.replace("/", "-")}T${scheduleEndTime.value}:00"
                } ?: "",
                startScheduleLocation.value,
                endScheduleLocation.value,
                scheduleRepetition.value
            )

            mainRepository.patchSchedule(patchScheduleBody)
                .catch {
                    Log.d("PLANJDEBUG", "ScheduleFragment Edit error ${it.message}")
                }
                .collect {
                    val newSchedule = Schedule(
                        scheduleId = scheduleId,
                        title = scheduleTitle.value,
                        memo = scheduleMemo.value,
                        startTime = if (scheduleStartDate.value == null) {
                            null
                        } else {
                            "${
                                scheduleStartDate.value?.replace(
                                    '/',
                                    '-'
                                )
                            }T${scheduleStartTime.value}:00"
                        },
                        endTime = "${
                            scheduleEndDate.value?.replace(
                                '/',
                                '-'
                            )
                        }T${scheduleEndTime.value}:00",
                        categoryTitle = scheduleCategory.value,
                        repetition = scheduleRepetition.value,
                        alarm = scheduleAlarm.value,
                        members = emptyList(),
                        doneMembers = doneMembers.value,
                        location = _endScheduleLocation.value,
                        isFinished = isFinished.value,
                        isFailed = isFailed.value
                    )
                    mainRepository.insertSchedule(newSchedule)
                    _isEditMode.value = false
                    Log.d("PLANJDEBUG", "ScheduleFragment Edit Success")
                }

        }
    }

    fun resetStartTime() {
        _scheduleStartDate.value = null
        _scheduleStartTime.value = null
    }

    fun endMapDelete() {
        _endScheduleLocation.value = null
    }

    fun startMapDelete() {
        startScheduleLocation.value = null
    }


    fun getNaverRoute(startLocation: Location?, endLocation: Location) {
        if (startLocation == null) return

        val start = "${startLocation.longitude},${startLocation.latitude}"
        val end = "${endLocation.longitude},${endLocation.latitude}"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _route.value = naverRepository.getNaverRoute(start, end)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "error ${e.message}")
            }
        }
    }

    fun emptyRoute() {
        _route.value = null
    }

    fun emptyStartLocation() {
        startScheduleLocation.value = null
    }
}