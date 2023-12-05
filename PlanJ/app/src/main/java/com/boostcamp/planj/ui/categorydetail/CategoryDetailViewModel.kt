package com.boostcamp.planj.ui.categorydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _cateogry = MutableStateFlow(Category("", ""))
    val category: StateFlow<Category> = _cateogry.asStateFlow()

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules = _schedules.asStateFlow()


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
            val getCategory = _cateogry.value.categoryUuid
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
        _cateogry.value = category
        viewModelScope.launch {
            getCategoryDetailSchedules()
        }
    }

    fun checkBoxChange(schedule: Schedule, isCheck: Boolean) {
        val calendar = Calendar.getInstance()
        val endTime = schedule.endAt

        calendar.set(
            endTime.year,
            endTime.month - 1,
            endTime.day,
            endTime.hour,
            endTime.minute,
            endTime.second
        )
        val fail = calendar.timeInMillis < System.currentTimeMillis()
    }

    fun getCategoryDetailSchedules() {
        Log.d("PLANJDEBUG", "getCategoryDetailSchedules call")
        viewModelScope.launch {
            if (category.value.categoryName == "전체 일정") {
                mainRepository.getSearchSchedules("").catch {
                    Log.d("PLANJDEBUG","getCategoryDetailSchedules All Schedule Error ${it.message}")
                }.collectLatest {
                    _schedules.value=it
                    Log.d("PLANJDEBUG","getCategoryDetailSchedules All Schedule Success")
                }
            } else {
                mainRepository.getCategorySchedulesApi(_cateogry.value.categoryUuid)
                    .catch {
                        Log.d("PLANJDEBUG", "getCategoryDetailSchedules Error ${it.message}")
                    }.collectLatest {
                        _schedules.value = it
                        Log.d("PLANJDEBUG", "getCategoryDetailSchedules Success")
                    }
            }
        }
    }

    fun getCategoryList():List<String>{
        mainRepository.getCategoryListApi()


        return emptyList()
    }

}