package com.boostcamp.planj.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.network.ApiResult
import com.boostcamp.planj.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    fun postSignIn() {
        viewModelScope.launch {
            val apiResult = loginRepository.postSignIn(userEmail.value, userPwd.value)
            when (apiResult) {
                is ApiResult.Success -> {
                    _isSuccess.value = true
                    _showToast.emit("로그인이 완료되었습니다.")
                }

                is ApiResult.Error -> {
                    when (apiResult.statusCode) {
                        401 -> _showToast.emit("이메일 또는 비밀번호가 일치하지 않습니다.")
                        else -> _showToast.emit("Error")
                    }
                }
            }
        }
    }
}