package com.boostcamp.planj.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

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
}