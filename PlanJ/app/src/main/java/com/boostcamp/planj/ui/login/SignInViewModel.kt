package com.boostcamp.planj.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    val userEmail = MutableStateFlow("")
    val userPwd = MutableStateFlow("")

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun postSignIn() {
        viewModelScope.launch {
            val result = loginRepository.postSignIn(userEmail.value, userPwd.value)
            _isSuccess.value = result
        }
    }
}