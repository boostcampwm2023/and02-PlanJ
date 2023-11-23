package com.boostcamp.planj.ui.categorydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()


    val schedules by lazy {
        mainRepository.getCategoryTitleSchedule(title.value)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }


    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteSchedule(schedule)
        }
    }


    fun insertSchedule(category: String, title: String, endTime: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val getCategory = mainRepository.getCategory(category)
            mainRepository.postSchedule("01HFYAR1FX09FKQ2SW1HTG8BJ8", getCategory.categoryId, title, endTime)
                .catch {
                    Log.d("PLANJDEBUG", "postSchedule error ${it.message}")
                }
                .collect {
                    val schedule = Schedule(
                        scheduleId =  it.data.scheduleUuid,
                        categoryTitle = category,
                        title = title,
                        endTime = endTime
                    )
                    mainRepository.insertSchedule(schedule)
                }

        }

        //retrofit 요청으로
        //응답받아야 할 것이 id
        //schedule.copy(scheduleId =  = )

        //viewModelScope.launch(Dispatchers.IO) { mainRepository.insertSchedule(schedule) }
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        mainRepository.getUser().first()
    }

}