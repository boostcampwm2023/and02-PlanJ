package com.boostcamp.planj.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.repository.LoginRepository
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val localAlarms = MutableStateFlow<List<AlarmInfo>>(emptyList())

    private val _newAlarms = MutableStateFlow<List<AlarmInfo>>(emptyList())
    val newAlarms = _newAlarms.asStateFlow()

    private val _deletedAlarms = MutableStateFlow<List<AlarmInfo>>(emptyList())
    val deletedAlarms = _deletedAlarms.asStateFlow()

    init {
        viewModelScope.launch {
            localAlarms.value = loginRepository.getAllAlarmInfo()
            mainRepository.getAlarms().collectLatest {
                _newAlarms.value = it
            }
            loginRepository.deleteAllAlarm()
            newAlarms.value.forEach {
                loginRepository.insertAlarmInfo(it)
            }
        }
        _deletedAlarms.value = localAlarms.value.filter { localAlarm ->
            newAlarms.value.find { it == localAlarm } == null
        }
    }
}