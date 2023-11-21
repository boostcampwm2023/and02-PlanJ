package com.boostcamp.planj.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun insertSchedule(schedule: Schedule) {

        //retrofit 요청으로
        //응답받아야 할 것이 id
        //schedule.copy(scheduleId =  = )

        viewModelScope.launch(Dispatchers.IO) { mainRepository.insertSchedule(schedule) }
    }




}

