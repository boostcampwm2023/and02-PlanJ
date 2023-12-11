package com.boostcamp.planj.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _selectDate = MutableStateFlow("")
    val selectDate = _selectDate.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _calendarTitle = MutableStateFlow("")
    val calendarTitle = _calendarTitle.asStateFlow()

    private val _isCurrent = MutableStateFlow(true)
    val isCurrent = _isCurrent.asStateFlow()

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules = _schedules.asStateFlow()

    private val _failedSchedule = MutableStateFlow<Schedule?>(null)
    val failedSchedule = _failedSchedule.asStateFlow()

    private val _scheduleSegment = MutableStateFlow<List<ScheduleSegment>>(emptyList())
    val scheduleSegment = _scheduleSegment.asStateFlow()

    val isRefreshing = MutableStateFlow(false)

    var listener: OnClickListener = OnClickListener { }

    var currentPosition = Int.MAX_VALUE / 2

    init {
        initSetDate()
        getAllSchedule()
    }

    fun postSchedule(category: String, title: String) {
        val date = _selectDate.value.split("-").map { it.toInt() }
        val dateTime = DateTime(date[0], date[1], date[2], 23, 59)
        viewModelScope.launch(Dispatchers.IO) {
            categories.value.find { it.categoryName == category }?.let { c ->
                mainRepository.postSchedule(c.categoryUuid, title, dateTime)
                    .catch {
                        Log.d("PLANJDEBUG", "postSchedule error ${it.message}")
                    }
                    .collectLatest {
                        getScheduleDaily(dateTime.toFormattedString())
                    }
            }
        }
    }

    fun refresh() {
        isRefreshing.value = true
        getScheduleDaily("${_selectDate.value}T00:00:00")
    }

    fun getScheduleDaily(date: String) {
        viewModelScope.launch {
            mainRepository.getDailyScheduleApi(date)
                .catch {
                    Log.d("PLANJDEBUG", "getScheduleDaily Error ${it.message}")
                }
                .collectLatest {
                    _schedules.value = it
                    isRefreshing.value = false
                }
        }
    }

    fun setCalendarTitle(title: String) {
        _calendarTitle.value = title
    }

    private fun getAllSchedule() {
        viewModelScope.launch {
            mainRepository.getSearchSchedules("")
                .catch {
                    Log.d("PLANJDEBUG", "getAllSchedule error ${it.message}")
                }
                .collectLatest {
                    it.find { schedule -> schedule.isFinished && schedule.isFailed && !schedule.hasRetrospectiveMemo }
                        ?.let { s ->
                            _failedSchedule.value = s
                        }
                }
        }
    }

    private fun initSetDate() {
        val calendar = Calendar.getInstance()
        setDate(
            "${calendar.get(Calendar.YEAR)}-${
                String.format(
                    "%02d",
                    calendar.get(Calendar.MONTH) + 1
                )
            }-${String.format("%02d", calendar.get(Calendar.DATE))}"
        )
        calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DAY_OF_WEEK))
        val currentDate = SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(calendar.time)
        setCalendarTitle(currentDate)
    }


    fun setDate(date: String) {
        _selectDate.value = date
    }

    fun setIsCurrent(position: Int) {
        currentPosition = position
        val now = LocalDate.now()
        _isCurrent.value = (_selectDate.value == "${now.year}-${
            String.format(
                "%02d",
                now.monthValue
            )
        }-${String.format("%02d", now.dayOfMonth)}") && (position == Int.MAX_VALUE / 2)
    }

    fun deleteSchedule(scheduleId: String) {
        viewModelScope.launch {
            try {
                mainRepository.deleteScheduleApi(scheduleId)
                getScheduleDaily("${_selectDate.value}T00:00:00")
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "deleteSchedule Error ${e.message}")
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            mainRepository.getCategoryListApi().catch {
                Log.d("PLANJDEBUG", "getCategories Error ${it.message}")
            }.collect {
                val list = it.toMutableList()
                list.add(0, Category("default", "미분류"))
                _categories.value = list
            }
        }

    }

    fun scheduleFinishChange(schedule: Schedule, showDialog: (Schedule) -> Unit) {
        viewModelScope.launch {
            mainRepository.getScheduleChecked(schedule.scheduleId).catch {
                Log.d("PLANJDEBUG", "getScheduleChecked Error ${it.message}")
            }.collectLatest {
                if (it.data.failed && !it.data.hasRetrospectiveMemo) {
                    showDialog(schedule)
                }
                getScheduleDaily("${_selectDate.value}T00:00:00")
            }
        }
    }

    fun postScheduleAddMemo(schedule: Schedule, memo: String) {
        viewModelScope.launch {
            try {
                mainRepository.postScheduleAddMemo(schedule.scheduleId, memo)
                _failedSchedule.value = null
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "postScheduleAddMemo error ${e.message}")
            }
        }
    }

    fun setScheduleSegment(scheduleSegment: List<ScheduleSegment>) {
        _scheduleSegment.value = scheduleSegment
    }

    fun changeExpanded(index: Int) {
        val list = _scheduleSegment.value.toMutableList()
        list[index] = list[index].copy(expanded = !list[index].expanded)
        _scheduleSegment.value = list
    }
}

