package com.boostcamp.planj.ui

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Repetition
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
fun TextView.setMemoLength(memo: String?) {
    val memoLength = "${memo?.length ?: 0}/255"
    text = memoLength
}

@BindingAdapter("repetitionInfo")
fun TextView.setRepetitionInfo(repetition: Repetition?) {
    text = if (repetition == null) {
        "설정 안함"
    } else if (repetition.cycleType == "daily") {
        "${repetition.cycleCount}일마다 반복"
    } else {
        "${repetition.cycleCount}주마다 반복"
    }
}

@BindingAdapter("alarmInfo")
fun TextView.setAlarmInfo(alarmInfo: Alarm?) {
    text = if (alarmInfo != null && alarmInfo.alarmType == "departure") {
        "출발 시간 ${alarmInfo.alarmTime}분 전"
    } else if (alarmInfo != null && alarmInfo.alarmType == "end") {
        "종료 시간 ${alarmInfo.alarmTime}분 전"
    } else {
        "설정 안함"
    }
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
    visibility = if (!schedule.isFinished && schedule.isFailed) {
        View.VISIBLE
    } else
        View.GONE
}

@BindingAdapter("setTitle")
fun TextView.setTitle(schedule: Schedule) {
    if (schedule.isFinished) {
        paintFlags =
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
    if (schedule.isFailed) {
        setTextColor(Color.RED)
    }
    text = schedule.title
}


@BindingAdapter("setCategoryBackground")
fun LinearLayout.setBackground(item: Category) {
    if (item.categoryId == "0")
        setBackgroundResource(R.drawable.round_r8_main1)
    else
        setBackgroundResource(R.drawable.round_r8_main2)
}

@BindingAdapter("isPopUpMenuVisible")
fun TextView.isPopUpMenuVisible(category: Category) {
    visibility = if (category.categoryName == "전체 일정" || category.categoryName == "미분류") {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}