package com.boostcamp.planj.ui

import androidx.databinding.BindingAdapter
import com.boostcamp.planj.ui.login.EmailState
import com.boostcamp.planj.ui.login.PwdState
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("emailError")
fun TextInputLayout.setEmailError(emailState: EmailState) {
    error = when (emailState) {
        EmailState.ERROR_FORMAT -> "이메일 형식이 옳지 않습니다."
        EmailState.ERROR_EXIST -> "이미 존재하는 이메일입니다."
        else -> null
    }
}

@BindingAdapter("pwdError")
fun TextInputLayout.setPwdError(pwdState: PwdState) {
    error = when (pwdState) {
        PwdState.ERROR_LENGTH -> "비밀번호 길이는 8자 이상 16자 이하입니다."
        PwdState.ERROR_CONTENTS -> "비밀번호는 영문, 숫자, 특수문자를 1자이상 포함해야 합니다."
        else -> null
    }
}