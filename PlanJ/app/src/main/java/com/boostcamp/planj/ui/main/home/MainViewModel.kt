package com.boostcamp.planj.ui.main.home

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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

    fun getScheduleDaily(date: String) {
        viewModelScope.launch {
            mainRepository.getDailyScheduleApi(date)
                .catch {
                    Log.d("PLANJDEBUG", "getScheduleDaily Error ${it.message}")
                }
                .collectLatest {
                    _schedules.value = it
                }
        }
    }

    fun setCalendarTitle(title: String) {
        _calendarTitle.value = title
    }


    fun setDate(date: String) {
        _selectDate.value = date
    }

    fun setIsCurrent(position: Int) {
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
                Log.d("PLANJDEBUG", "getCategories Success $it")
                val list = it.toMutableList()
                list.add(0, Category("default", "미분류"))
                _categories.value = list
            }
        }

    }

    fun scheduleFinishChange(schedule: Schedule, showDialog : (Schedule) -> Unit) {
        viewModelScope.launch {
            mainRepository.getScheduleChecked(schedule.scheduleId).catch {
                Log.d("PLANJDEBUG","getScheduleChecked Error ${it.message}")
            }.collectLatest {
                if(it.data.failed&& !it.data.hasRetrospectiveMemo){
                    showDialog(schedule)
                }
                getScheduleDaily("${_selectDate.value}T00:00:00")
            }

        }
    }

}

