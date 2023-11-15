package com.boostcamp.planj.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    val userEmail = MutableLiveData<String>()
    val userPwd = MutableLiveData<String>()
    val userPwdConfirm = MutableLiveData<String>()
    val userNickname = MutableLiveData<String>()

    private val _emailState = MutableLiveData<EmailState>()
    val emailState: LiveData<EmailState> = _emailState

    private val _pwdState = MutableLiveData<PwdState>()
    val pwdState: LiveData<PwdState> = _pwdState

    private val _pwdConfirmState = MutableLiveData<PwdConfirmState>()
    val pwdConfirmState: LiveData<PwdConfirmState> = _pwdConfirmState

    private val _nicknameState = MutableLiveData<NicknameState>()
    val nicknameState: LiveData<NicknameState> = _nicknameState

    private val _isEnable = MutableLiveData(false)
    val isEnable: LiveData<Boolean> = _isEnable

    private val _isComplete = MutableLiveData<Boolean>()
    val isComplete: LiveData<Boolean> = _isComplete

    private val regexEmail = Regex("""^([A-z0-9_\-.]+)@([A-z0-9_\-]+)\.([a-zA-Z]{2,5})$""")
    private val regexEnglish = Regex("""[A-z]""")
    private val regexNum = Regex("""[0-9]""")
    private val regexSpecial = Regex("""[~`!@#\\$%^&*()_\-+=]""")

    fun checkEmail() {
        userEmail.value?.let { userEmail ->
            _emailState.value =
                if (regexEmail.matches(userEmail)) EmailState.AVAILABLE else EmailState.ERROR_FORMAT
        }

        checkEnable()
    }

    fun checkPwd() {
        userPwd.value?.let { userPwd ->
            _pwdState.value = if (userPwd.length < 8 || userPwd.length > 16) {
                PwdState.ERROR_LENGTH
            } else {
                if (!userPwd.contains(regexEnglish)) PwdState.ERROR_ENGLISH
                else if (!userPwd.contains(regexNum)) PwdState.ERROR_NUM
                else if (!userPwd.contains(regexSpecial)) PwdState.ERROR_SPECIAL
                else PwdState.AVAILABLE
            }

            userPwdConfirm.value?.let { userPwdConfirm ->
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
            if (!userPwdConfirm.value.isNullOrEmpty() && !userPwd.value.isNullOrEmpty() && userPwdConfirm.value == userPwd.value)
                PwdConfirmState.AVAILABLE
            else
                PwdConfirmState.ERROR

        checkEnable()
    }

    fun checkNickname() {
        userNickname.value?.let { userNickname ->
            _nicknameState.value =
                if (userNickname.length in 1..12) NicknameState.AVAILABLE else NicknameState.ERROR_LENGTH
        }

        checkEnable()
    }

    private fun checkEnable() {
        _isEnable.value =
            (emailState.value == EmailState.AVAILABLE && pwdState.value == PwdState.AVAILABLE && pwdConfirmState.value == PwdConfirmState.AVAILABLE && nicknameState.value == NicknameState.AVAILABLE)
    }

    fun postSignUp() {
        viewModelScope.launch {
            // TODO: 결과에 따른 처리 필요
            val result = loginRepository.postSignUp(
                userEmail.value.toString(),
                userPwd.value.toString(),
                userNickname.value.toString()
            )
            _isComplete.value = result
        }
    }
}