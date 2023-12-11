package com.boostcamp.planj.ui.categorydetail

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _category = MutableStateFlow(Category("", ""))
    val category: StateFlow<Category> = _category.asStateFlow()

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules = _schedules.asStateFlow()

    private val _scheduleSegment = MutableStateFlow<List<ScheduleSegment>>(emptyList())
    val scheduleSegment = _scheduleSegment.asStateFlow()

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainRepository.deleteScheduleApi(schedule.scheduleId)
                getCategoryDetailSchedules()
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "deleteSchedule Error ${e.message}")
            }
        }
    }

    fun postSchedule(category: String, title: String, endTime: DateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            val getCategory = _category.value.categoryUuid
            mainRepository.postSchedule(getCategory, title, endTime)
                .catch {
                    Log.d("PLANJDEBUG", "postSchedule error ${it.message}")
                }
                .collect {
                    getCategoryDetailSchedules()
                }
        }
    }

    fun setTitle(category: Category) {
        _category.value = category
        viewModelScope.launch {
            getCategoryDetailSchedules()
        }
    }

    fun getCategoryDetailSchedules() {
        Log.d("PLANJDEBUG", "getCategoryDetailSchedules call")
        viewModelScope.launch {
            if (category.value.categoryName == "전체 일정") {
                mainRepository.getSearchSchedules("").catch {
                    Log.d(
                        "PLANJDEBUG",
                        "getCategoryDetailSchedules All Schedule Error ${it.message}"
                    )
                }.collectLatest {
                    _schedules.value = it
                }
            } else {
                mainRepository.getCategorySchedulesApi(_category.value.categoryUuid)
                    .catch {
                        Log.d("PLANJDEBUG", "getCategoryDetailSchedules Error ${it.message}")
                    }.collectLatest {
                        _schedules.value = it
                    }
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
                getCategoryDetailSchedules()
            }
        }
    }

    fun postScheduleAddMemo(schedule: Schedule, memo: String) {
        viewModelScope.launch {
            try {
                mainRepository.postScheduleAddMemo(schedule.scheduleId, memo)
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