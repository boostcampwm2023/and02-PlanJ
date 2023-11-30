package com.boostcamp.planj.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val userInput = MutableStateFlow("")
    private val scheduleList = MutableStateFlow<List<Schedule>?>(null)
    private val _filteredScheduleList = MutableStateFlow<List<Schedule>?>(null)
    val filteredScheduleList: StateFlow<List<Schedule>?> = _filteredScheduleList


    private val isFiltered = MutableStateFlow(false)

    fun onClickSearch() {
        // TODO: userInput.value 이용해서 데이터 요청
        viewModelScope.launch {
            mainRepository.searchSchedule(userInput.value).collectLatest { resultList ->
                scheduleList.value = resultList
                filterSchedules()
            }
        }
    }

    fun onSwitchClicked() {
        isFiltered.value = !isFiltered.value
        filterSchedules()
    }

    private fun filterSchedules() {
        _filteredScheduleList.value = if (isFiltered.value) {
            scheduleList.value?.filter { !it.isFinished }
        } else {
            scheduleList.value
        }
    }
}