package com.boostcamp.planj.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Address
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ScheduleMapViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    private val _clicked = MutableStateFlow(false)
    val clicked: StateFlow<Boolean> = _clicked.asStateFlow()

    private val _searchMap = MutableStateFlow<List<Address>>(emptyList())
    val searchMap: StateFlow<List<Address>> = _searchMap.asStateFlow()

    fun searchMap(query: String) {
        viewModelScope.launch {
            try {
                _searchMap.value = searchRepository.searchMap(query)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "SearchMap Error")
            }
        }
    }

    fun setLocation(location : Location?){
        _location.value = location
    }


    fun changeClick() {
        _clicked.value = !_clicked.value
    }
}