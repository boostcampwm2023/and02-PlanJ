package com.boostcamp.planj.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Address
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.data.model.naver.NaverResponse
import com.boostcamp.planj.data.repository.NaverRepository
import com.boostcamp.planj.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleStartMapViewModel @Inject constructor(
    private val naverRepository: NaverRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _startLocation = MutableStateFlow<Location?>(null)
    val startLocation: StateFlow<Location?> = _startLocation.asStateFlow()


    private val _clicked = MutableStateFlow(false)
    val clicked: StateFlow<Boolean> = _clicked.asStateFlow()

    private val _searchMap = MutableStateFlow<List<Address>>(emptyList())
    val searchMap: StateFlow<List<Address>> = _searchMap.asStateFlow()

    private val _route = MutableStateFlow<NaverResponse?>(null)
    val route: StateFlow<NaverResponse?> = _route.asStateFlow()

    fun searchMap(query: String) {
        viewModelScope.launch {
            try {
                _searchMap.value = searchRepository.searchMap(query)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "SearchMap Error")
            }
        }
    }

    fun initLocation(startLocation: Location?) {
        _startLocation.value = startLocation
        _clicked.value = true
    }


    fun setLocation(location: Location?) {
        _startLocation.value = location
    }


    fun changeClick() {
        _clicked.value = !_clicked.value
    }

    fun getNaverRoute(startLocation: Location?, endLocation: Location) {
        if (startLocation == null) return

        val start = "${startLocation.longitude},${startLocation.latitude}"
        val end = "${endLocation.longitude},${endLocation.latitude}"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _route.value = naverRepository.getNaverRoute(start, end)
            }catch (e : Exception){
                Log.d("PLANJDEBUG", "error ${e.message}")
            }
        }
    }
}