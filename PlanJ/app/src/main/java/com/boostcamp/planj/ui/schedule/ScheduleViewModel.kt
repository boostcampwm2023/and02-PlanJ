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
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.NaverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

sealed class AlarmEvent {
    data class Delete(val scheduleId: String) : AlarmEvent()
    data class Set(val alarmInfo: AlarmInfo) : AlarmEvent()
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val naverRepository: NaverRepository
) : ViewModel() {

    private lateinit var scheduleId: String
    private val isFinished = MutableStateFlow(false)
    private val isFailed = MutableStateFlow(false)

    val scheduleCategory = MutableStateFlow("")
    val scheduleTitle = MutableStateFlow("")

    private val _scheduleStartTime = MutableStateFlow<DateTime?>(null)
    val scheduleStartTime: StateFlow<DateTime?> = _scheduleStartTime

    private val _scheduleEndTime = MutableStateFlow(DateTime(0, 0, 0, 0, 0))
    val scheduleEndTime: StateFlow<DateTime> = _scheduleEndTime

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

    val categoryList: StateFlow<List<String>> =
        mainRepository.getCategories()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    private val _response = MutableStateFlow<NaverResponse?>(null)
    val response: StateFlow<NaverResponse?> = _response.asStateFlow()

    private val _alarmEventFlow = MutableSharedFlow<AlarmEvent>()
    val alarmEventFlow: SharedFlow<AlarmEvent> = _alarmEventFlow

    fun setScheduleInfo(schedule: Schedule?) {
        schedule?.let { schedule ->
            scheduleId = schedule.scheduleId
            scheduleCategory.value = schedule.categoryName
            scheduleTitle.value = schedule.title
            _scheduleStartTime.value = schedule.startAt
            _scheduleEndTime.value = schedule.endAt
            _scheduleRepetition.value = schedule.repetition
            _scheduleAlarm.value = schedule.alarm
            //_doneMembers.value = schedule.doneMembers
            //_members.value = schedule.members
            _endScheduleLocation.value = schedule.endLocation
            startScheduleLocation.value = schedule.startLocation
            isFinished.value = schedule.isFinished
            isFailed.value = schedule.isFailed
            //scheduleMemo.value = schedule.remindMemo
        }
    }

    fun getStartDate(): Long? {
        return scheduleStartTime.value?.toMilliseconds()
    }

    fun setStartTime(millis: Long, hour: Int, minute: Int) {
        _scheduleStartTime.update {
            val (year, month, day) = changeMillisToDate(millis)
            DateTime(year, month, day, hour, minute)
        }
    }

    fun getEndDate(): Long {
        return scheduleEndTime.value.toMilliseconds()
    }

    fun setEndTime(millis: Long, hour: Int, minute: Int) {
        _scheduleEndTime.update {
            val (year, month, day) = changeMillisToDate(millis)
            DateTime(year, month, day, hour, minute)
        }
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

    private fun changeMillisToDate(millis: Long): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis

        return Triple(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun startEditingSchedule() {
        _isEditMode.value = true
    }

    fun deleteSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 등록된 알림이 있다면 삭제
                mainRepository.deleteAlarmInfoUsingScheduleId(scheduleId)
                mainRepository.getAlarmMode().collectLatest { alarmMode ->
                    if (alarmMode) {
                        _alarmEventFlow.emit(AlarmEvent.Delete(scheduleId))
                    }
                }

                mainRepository.deleteScheduleApi(scheduleId)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "scheduleFragment Delete error ${e.message}")
            }
        }
    }

    fun completeEditingSchedule() {
        // 시작시간 > 종료시간
        if (scheduleStartTime.value != null && (scheduleStartTime.value!!.toMilliseconds() > scheduleEndTime.value.toMilliseconds())) return
        // 현재 > 종료시간
        if (System.currentTimeMillis() > scheduleEndTime.value.toMilliseconds()) return

        setAlarmInfo()

        viewModelScope.launch(Dispatchers.IO) {
            val getCategory =
                withContext((Dispatchers.IO)) { mainRepository.getCategory(scheduleCategory.value) }

            val patchScheduleBody = PatchScheduleBody(
                getCategory.categoryId,
                scheduleId,
                scheduleTitle.value,
                scheduleMemo.value,
                scheduleStartTime.value?.toFormattedString(),
                scheduleEndTime.value.toFormattedString(),
                startScheduleLocation.value,
                endScheduleLocation.value,
                scheduleRepetition.value
            )

            mainRepository.patchSchedule(patchScheduleBody)
                .catch {
                    Log.d("PLANJDEBUG", "ScheduleFragment Edit error ${it.message}")
                }
                .collect {
                    _isEditMode.value = false
                    Log.d("PLANJDEBUG", "ScheduleFragment Edit Success")
                }
        }
    }

    private fun setAlarmInfo() {
        if (scheduleAlarm.value == null) {
            viewModelScope.launch {
                mainRepository.deleteAlarmInfoUsingScheduleId(scheduleId)
                mainRepository.getAlarmMode().collectLatest { alarmMode ->
                    if (alarmMode) {
                        _alarmEventFlow.emit(AlarmEvent.Delete(scheduleId))
                    }
                }
            }
        } else {
            val estimatedTimeInMillis = if (response.value != null) {
                response.value!!.route.trafast[0].summary.duration
            } else {
                0
            }
            val alarmInfo = AlarmInfo(
                scheduleId,
                scheduleTitle.value,
                scheduleEndTime.value,
                scheduleRepetition.value,
                scheduleAlarm.value!!,
                estimatedTimeInMillis / (1000 * 60)
            )

            // db에는 무조건 알람 정보 저장
            viewModelScope.launch {
                mainRepository.insertAlarmInfo(alarmInfo)
                mainRepository.getAlarmMode().collectLatest { alarmMode ->
                    // 알람 모드 켜져 있을 경우에만 알람매니저를 통해 알람 설정
                    if (alarmMode) {
                        _alarmEventFlow.emit(AlarmEvent.Set(alarmInfo))
                    }
                }
            }
        }
    }

    fun resetStartTime() {
        _scheduleStartTime.update {
            null
        }
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
                _response.value = naverRepository.getNaverRoute(start, end)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "error ${e.message}")
            }
        }
    }

    fun emptyRoute() {
        _response.value = null
    }

    fun emptyStartLocation() {
        startScheduleLocation.value = null
    }
}