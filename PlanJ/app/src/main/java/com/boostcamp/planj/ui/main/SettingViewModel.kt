package com.boostcamp.planj.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SettingViewModel : ViewModel() {

    private val isAlarmOn = MutableStateFlow(false)

    fun onClickAlarmSwitch() {
        isAlarmOn.value = !isAlarmOn.value
    }

    fun onClickLogout() {

    }

    fun onClickWithdrawal() {

    }
}