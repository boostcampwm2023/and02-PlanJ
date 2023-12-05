package com.boostcamp.planj.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.dto.PatchScheduleBody
import com.boostcamp.planj.data.model.naver.NaverResponse
import com.boostcamp.planj.data.repository.LoginRepository
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.NaverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

sealed class AlarmEvent {
    data class Delete(val scheduleId: String) : AlarmEvent()
    data class Set(val alarmInfo: AlarmInfo) : AlarmEvent()
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val naverRepository: NaverRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private lateinit var scheduleId: String

    val scheduleCategory = MutableStateFlow("")
    val scheduleTitle = MutableStateFlow("")

    private val _participants = MutableStateFlow<List<Participant>>(emptyList())
    val participants = _participants.asStateFlow()

    private val _scheduleStartTime = MutableStateFlow<DateTime?>(null)
    val scheduleStartTime = _scheduleStartTime.asStateFlow()

    private val _scheduleEndTime = MutableStateFlow(DateTime(0, 0, 0, 0, 0))
    val scheduleEndTime = _scheduleEndTime.asStateFlow()

    private val _scheduleAlarm = MutableStateFlow<Alarm?>(null)
    val scheduleAlarm = _scheduleAlarm.asStateFlow()

    private val _scheduleRepetition = MutableStateFlow<Repetition?>(null)
    val scheduleRepetition = _scheduleRepetition.asStateFlow()

    private val _startScheduleLocation = MutableStateFlow<Location?>(null)
    val startScheduleLocation = _startScheduleLocation.asStateFlow()

    private val _endScheduleLocation = MutableStateFlow<Location?>(null)
    val endScheduleLocation = _endScheduleLocation.asStateFlow()

    val scheduleMemo = MutableStateFlow<String?>(null)

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _isComplete = MutableStateFlow(false)
    val isComplete = _isComplete.asStateFlow()

    private val _response = MutableStateFlow<NaverResponse?>(null)
    val response = _response.asStateFlow()

    private val _alarmEventFlow = MutableSharedFlow<AlarmEvent>()
    val alarmEventFlow = _alarmEventFlow.asSharedFlow()

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    fun setScheduleId(newScheduleId: String?) {
        scheduleId = newScheduleId ?: ""
        getScheduleInfo()
    }

    private fun getScheduleInfo() {
        viewModelScope.launch {
            mainRepository.getDetailSchedule(scheduleId)
                .catch {
                    Log.d("PLANJDEBUG", "scheduleFragment getScheduleDetail error ${it.message}")
                }
                .collectLatest { schedule ->
                    scheduleCategory.value = schedule.categoryName
                    scheduleTitle.value = schedule.title
                    _scheduleStartTime.value = schedule.startAt
                    _scheduleEndTime.value = schedule.endAt
                    _scheduleRepetition.value = schedule.repetition
                    _scheduleAlarm.value = schedule.alarm
                    _participants.value = schedule.participants
                    _endScheduleLocation.value = schedule.endLocation
                    _startScheduleLocation.value = schedule.startLocation
                    scheduleMemo.value = schedule.description
                }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            mainRepository.getCategoryListApi().collectLatest { categories ->
                _categoryList.value = listOf(Category("default", "미분류")) + categories
            }
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
        _startScheduleLocation.value = startLocation
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
        viewModelScope.launch {
            try {
                // 등록된 알림이 있다면 삭제
                loginRepository.deleteAlarmInfoUsingScheduleId(scheduleId)
                loginRepository.getAlarmMode().collectLatest { alarmMode ->
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
        scheduleStartTime.value?.let { startTime ->
            // 시작시간 > 종료시간
            if (startTime.toMilliseconds() > scheduleEndTime.value.toMilliseconds()) {
                viewModelScope.launch {
                    _showToast.emit("종료 시각이 시작 시간보다 빠릅니다.")
                }
                return
            }

            // 반복 주기 < 일정 시간
            scheduleRepetition.value?.let { repetition ->
                val oneDayMillis = 24 * 60 * 60 * 1000L
                val interval =
                    if (repetition.cycleType == "DAILY") repetition.cycleCount else repetition.cycleCount * 7
                if (interval * oneDayMillis < scheduleEndTime.value.toMilliseconds() - startTime.toMilliseconds()) {
                    viewModelScope.launch {
                        _showToast.emit("일정 반복 주기가 일정보다 짧습니다.")
                    }
                    return
                }
            }
        }
        // 현재 > 종료시간
        if (System.currentTimeMillis() > scheduleEndTime.value.toMilliseconds()) {
            viewModelScope.launch {
                _showToast.emit("종료 시각이 현재 시간보다 빠릅니다.")
            }
            return
        }

        setAlarmInfo()

        viewModelScope.launch {
            categoryList.value.find { it.categoryName == scheduleCategory.value }?.let { category ->
                val patchScheduleBody = PatchScheduleBody(
                    categoryUuid = category.categoryUuid,
                    scheduleUuid = scheduleId,
                    title = scheduleTitle.value,
                    description = scheduleMemo.value,
                    startAt = scheduleStartTime.value?.toFormattedString(),
                    endAt = scheduleEndTime.value.toFormattedString(),
                    startLocation = startScheduleLocation.value,
                    endLocation = endScheduleLocation.value,
                    repetition = scheduleRepetition.value,
                    participants = participants.value,
                    alarm = scheduleAlarm.value
                )

                mainRepository.patchSchedule(patchScheduleBody)
                    .catch {
                        Log.d("PLANJDEBUG", "ScheduleFragment Edit error ${it.message}")
                    }
                    .collect {
                        _isEditMode.value = false
                        _showToast.emit("일정을 수정했습니다.")
                        Log.d("PLANJDEBUG", "ScheduleFragment Edit Success")
                    }
            }
        }
    }

    private fun setAlarmInfo() {
        if (scheduleAlarm.value == null) {
            viewModelScope.launch {
                loginRepository.deleteAlarmInfoUsingScheduleId(scheduleId)
                loginRepository.getAlarmMode().collectLatest { alarmMode ->
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
                loginRepository.insertAlarmInfo(alarmInfo)
                loginRepository.getAlarmMode().collectLatest { alarmMode ->
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
        _startScheduleLocation.value = null
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
        _startScheduleLocation.value = null
    }
}