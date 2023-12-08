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
class SignUpViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    val userEmail = MutableStateFlow("")
    val userPwd = MutableStateFlow("")
    val userPwdConfirm = MutableStateFlow("")
    val userNickname = MutableStateFlow("")

    private val _emailState = MutableStateFlow(EmailState.NONE)
    val emailState: StateFlow<EmailState> = _emailState

    private val _pwdState = MutableStateFlow(PwdState.NONE)
    val pwdState: StateFlow<PwdState> = _pwdState

    private val _pwdConfirmState = MutableStateFlow(PwdConfirmState.NONE)
    val pwdConfirmState: StateFlow<PwdConfirmState> = _pwdConfirmState

    private val _nicknameState = MutableStateFlow(NicknameState.NONE)
    val nicknameState: StateFlow<NicknameState> = _nicknameState

    private val _isEnable = MutableStateFlow(false)
    val isEnable: StateFlow<Boolean> = _isEnable

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    private val regexEmail = Regex("""^([A-z0-9_\-.]+)@([A-z0-9_\-]+)\.([a-zA-Z]{2,5})$""")
    private val regexEnglish = Regex("""[A-z]""")
    private val regexNum = Regex("""[0-9]""")
    private val regexSpecial = Regex("""[~`!@#\\$%^&*()_\-+=]""")

    fun checkEmail() {
        userEmail.value.let { userEmail ->
            _emailState.value =
                if (regexEmail.matches(userEmail)) EmailState.AVAILABLE else EmailState.ERROR_FORMAT
        }

        checkEnable()
    }

    fun checkPwd() {
        userPwd.value.let { userPwd ->
            _pwdState.value = if (userPwd.length < 8 || userPwd.length > 16) {
                PwdState.ERROR_LENGTH
            } else if (!userPwd.contains(regexEnglish) || !userPwd.contains(regexNum) || !userPwd.contains(
                    regexSpecial
                )
            ) {
                PwdState.ERROR_CONTENTS
            } else {
                PwdState.AVAILABLE
            }

            userPwdConfirm.value.let { userPwdConfirm ->
                if (userPwdConfirm.isNotEmpty()) {
                    _pwdConfirmState.value =
                        if (userPwd == userPwdConfirm) PwdConfirmState.AVAILABLE else PwdConfirmState.ERROR
                }
            }

            checkEnable()
        }
    }

    fun checkPwdConfirm() {
        _pwdConfirmState.value =
            if (userPwdConfirm.value == userPwd.value) PwdConfirmState.AVAILABLE else PwdConfirmState.ERROR

        checkEnable()
    }

    fun checkNickname() {
        userNickname.value.let { userNickname ->
            _nicknameState.value =
                if (userNickname.length in 2..12 && ("^[a-zA-Z0-9ㄱ-ㅎ가-힣]+$".toRegex()).matches(
                        userNickname
                    )
                ) NicknameState.AVAILABLE else NicknameState.ERROR
        }

        checkEnable()
    }

    private fun checkEnable() {
        _isEnable.value =
            (emailState.value == EmailState.AVAILABLE && pwdState.value == PwdState.AVAILABLE && pwdConfirmState.value == PwdConfirmState.AVAILABLE && nicknameState.value == NicknameState.AVAILABLE)
    }

    fun postSignUp() {
        viewModelScope.launch {
            val apiResult =
                loginRepository.postSignUp(userEmail.value, userPwd.value, userNickname.value)
            when (apiResult) {
                is ApiResult.Success -> {
                    _isComplete.value = true
                    _showToast.emit("회원가입이 완료되었습니다.")
                }

                is ApiResult.Error -> {
                    when (apiResult.statusCode) {
                        409 -> _emailState.value = EmailState.ERROR_EXIST
                        else -> _showToast.emit("Error")
                    }
                }
            }
        }
    }
}