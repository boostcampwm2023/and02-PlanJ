package com.boostcamp.planj.ui.categorydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules = _schedules.asStateFlow()



    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            //TODO api로 변경
        }
    }

    fun postSchedule(category: String, title: String, endTime: DateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            val getCategory = mainRepository.getCategory(category)
            mainRepository.postSchedule(getCategory.categoryId, title, endTime)
                .catch {
                    Log.d("PLANJDEBUG", "postSchedule error ${it.message}")
                }
                .collect {
//                    val schedule = Schedule(
//                        scheduleId = it.data.scheduleUuid,
//                        categoryName = category,
//                        title = title,
//                        endAt = endTime
//                    )

                    //TODO post 성공 후 다시 api 요청하기
                }
        }
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        mainRepository.getToken().first()
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

}