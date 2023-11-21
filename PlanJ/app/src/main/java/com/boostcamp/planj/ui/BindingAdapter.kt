package com.boostcamp.planj.ui

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
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

@BindingAdapter("memoLength")
fun TextView.setMemoLength(memo: String?){
    val memoLength = "${memo?.length ?: 0}/255"
    text = memoLength
}

@BindingAdapter("participation")
fun TextView.setParticipation(schedule: Schedule) {
    if (schedule.members.size < 2) {
        visibility = View.GONE
        return
    }
    text = "${schedule.doneMembers?.size ?: 0} / ${schedule.members.size}"
}

@BindingAdapter("checkFail")
fun ImageView.checkFail(schedule: Schedule) {
    visibility = if (!schedule.finished && schedule.failed) {
        View.VISIBLE
    } else
        View.GONE
}

@BindingAdapter("setTitle")
fun TextView.setTitle(schedule: Schedule) {
    if (schedule.finished) {
        paintFlags =
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
    if (schedule.failed) {
        setTextColor(Color.RED)
    }
    text = schedule.title
}


@BindingAdapter("setCategoryBackground")
fun LinearLayout.setBackground(item : Category){
    if(item.categoryId == "0")
        setBackgroundResource(R.drawable.round_r8_main1)
    else
        setBackgroundResource(R.drawable.round_r8_main2)
}