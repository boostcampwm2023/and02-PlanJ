package com.boostcamp.planj.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    fun insertSchedule(category: String, title: String, endTime: DateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            categories.value.find { it.categoryName == category }?.let { c ->
                mainRepository.postSchedule(c.categoryId, title, endTime.toFormattedString())
                    .catch {
                        Log.d("PLANJDEBUG", "postSchedule error ${it.message}")
                    }
                    .collect {
                        val schedule = Schedule(
                            scheduleId = it.data.scheduleUuid,
                            categoryTitle = category,
                            title = title,
                            endTime = endTime
                        )
                        mainRepository.insertSchedule(schedule)
                    }
            }
        }

        //retrofit 요청으로
        //응답받아야 할 것이 id
        //schedule.copy(scheduleId =  = )

        //viewModelScope.launch(Dispatchers.IO) { mainRepository.insertSchedule(schedule) }
    }

    val categories =
        mainRepository.getAllCategories().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )

    suspend fun getUser() = withContext(Dispatchers.IO) {
        mainRepository.getToken().first()
    }
}

