package com.boostcamp.planj.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.repository.LoginRepository
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
            mainRepository.getAlarms()
                .catch {
                    Log.d("PLANJDEBUG", "mainViewModel getAlarms ${it.message}")
                }
                .collectLatest { alarms ->
                    _newAlarms.value = alarms
                    loginRepository.deleteAllAlarm()
                    newAlarms.value.forEach { alarmInfo ->
                        loginRepository.insertAlarmInfo(alarmInfo)
                    }
                    loginRepository.isFirst()
                    _deletedAlarms.value = localAlarms.value.filter { localAlarm ->
                        newAlarms.value.find { it == localAlarm } == null
                    }
                }
        }
    }

    fun isFirst(): Boolean {
        var isFirst = true
        viewModelScope.launch {
            loginRepository.isFirst().collectLatest {
                isFirst = it
            }
        }
        return isFirst
    }

    fun saveFirst() {
        viewModelScope.launch {
            loginRepository.saveFirst(false)
        }
    }
}