package com.boostcamp.planj.ui.categorydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            //TODO api로 변경
        }
    }

    fun postSchedule(category: String, title: String, endTime: DateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            //TODO 카테고리 id 조회하기
            val getCategory = ""
            mainRepository.postSchedule(getCategory, title, endTime)
                .catch {
                    Log.d("PLANJDEBUG", "postSchedule error ${it.message}")
                }
                .collect {

                    //TODO post 성공 후 다시 api 요청하기
                }
        }
    }

    fun setTitle(category: Category) {
        _cateogry.value = category
        viewModelScope.launch {
            getScheduleInCategory(category)
        }
    }

    fun getScheduleInCategory(category: Category) {
        //TODO 카테고리 모든 일정 가져오기
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
            mainRepository.getCategorySchedulesApi(_cateogry.value.categoryUuid)
                .catch {
                    Log.d("PLANJDEBUG", "getCategoryDetailSchedules Error ${it.message}")
                }.collectLatest {
//                    _schedules.value = it
                    Log.d("PLANJDEBUG", "getCategoryDetailSchedules Success ${it}")
                }
        }
    }
}