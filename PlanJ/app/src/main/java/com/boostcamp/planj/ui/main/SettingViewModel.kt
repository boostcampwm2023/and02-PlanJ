package com.boostcamp.planj.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.repository.LoginRepository
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val isAlarmOn = MutableStateFlow(false)

    fun onClickAlarmSwitch() {
        isAlarmOn.value = !isAlarmOn.value
    }

    fun onClickLogout() {

    }

    fun onClickWithdrawal() {
        viewModelScope.launch {
            mainRepository.deleteAccount().runCatching {
                TODO("로그인 화면으로 이동")
            }
        }

    }
}