package com.boostcamp.planj.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.network.ApiResult
import com.boostcamp.planj.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    var deviceToken: String = ""
    fun postSignIn() {
        Log.d("PLANJDEBUG", "deviceToken $deviceToken")
        viewModelScope.launch {
            val apiResult = loginRepository.postSignIn(userEmail.value, userPwd.value, deviceToken)
            when (apiResult) {
                is ApiResult.Success -> {
                    _isSuccess.value = true
                    saveId(apiResult.data.uid.token)
                    _showToast.emit("로그인이 완료되었습니다.")
                }

                is ApiResult.Error -> {
                    when (apiResult.statusCode) {
                        401 -> _showToast.emit("이메일 또는 비밀번호가 일치하지 않습니다.")
                        else -> _showToast.emit("Error")
                    }
                }

                else -> {}
            }
        }
    }

    private suspend fun saveId(id: String) {
        withContext(Dispatchers.IO) {
            loginRepository.saveUser(id)
        }
    }

    fun postSignInNaver(accessToken: String) {
        Log.d("PLANJDEBUG", "deviceToken $deviceToken")
        viewModelScope.launch {
            loginRepository.postSignInNaver(accessToken, deviceToken)
                .catch {
                    Log.d("PLANJDEBUG", "postSignInNaver error : ${it.message}")
                }
                .collectLatest {
                    Log.d("PLANJDEBUG", "postSignInNaver success : $it")
                    _isSuccess.value = true
                    saveId(it.uid.token)
                    _showToast.emit("로그인이 완료되었습니다.")
                }
        }
    }

    suspend fun getToken() = withContext(Dispatchers.IO) {
        loginRepository.getToken().first()
    }
}