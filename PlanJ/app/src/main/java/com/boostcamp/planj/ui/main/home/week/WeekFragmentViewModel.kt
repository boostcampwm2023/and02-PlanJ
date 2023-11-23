package com.boostcamp.planj.ui.main.home.week

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.repository.MainRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class WeekFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepositoryImpl,
) : ViewModel() {

    val scheduleList =
        mainRepository.getWeekSchedule()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(50000), emptyList())

}