package com.boostcamp.planj.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
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

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    private val isFiltered = MutableStateFlow(false)

    fun onClickSearch() {
        viewModelScope.launch {
            mainRepository.getSearchSchedules(userInput.value).catch {
                Log.d("PLANJDEBUG", "onClickSearch getSearchSchedules Error ${it.message}")
                _showToast.emit("검색을 실패했습니다.")
            }.collectLatest {
                scheduleList.value = it
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