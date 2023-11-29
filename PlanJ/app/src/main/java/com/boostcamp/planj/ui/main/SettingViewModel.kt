package com.boostcamp.planj.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val isAlarmOn = MutableStateFlow(false)

    val userInfo = MutableStateFlow<User?>(null)

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    init {
        viewModelScope.launch {
            mainRepository.getMyInfo().collectLatest { user ->
                userInfo.value = user
            }
        }
    }

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

    fun changeEditMode(){
        _isEditMode.value = !_isEditMode.value
    }
}