package com.boostcamp.planj.ui

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.getDate
import com.boostcamp.planj.getTime
import com.boostcamp.planj.ui.login.EmailState
import com.boostcamp.planj.ui.login.PwdState
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Locale

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
fun TextView.setAlarmInfo(alarmInfo: String?) {
    text = if (alarmInfo.isNullOrEmpty()) "설정 안함" else alarmInfo
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

@BindingAdapter("setDateTime")
fun TextView.setDateTime(schedule: Schedule) {
    val currentDate =
        SimpleDateFormat("yyyy-MM-dd", Locale("kr", "ko")).format(System.currentTimeMillis())
    val endDate = schedule.endTime.getDate()
    val endTime = schedule.endTime.getTime().split(":").subList(0, 2).joinToString(":")
    if (schedule.startTime == null) {

        text = if(currentDate == endDate){
            "오늘 $endTime 까지"
        }else {
            "${endDate.replace("-", "/")} $endTime"
        }
    } else {
        val startDate = schedule.startTime.getDate()
        val startTime = schedule.startTime.getTime().split(":").subList(0, 2).joinToString(":")
        text = if(startDate == endDate){
            if(currentDate == startDate){
                "오늘 $startTime ~ $endTime"
            }else {
                "${startDate.replace("-", "/")} $startTime ~ $endTime"
            }
        }else {
            "${startDate.replace("-", "/")} - ${endDate.replace("-", "/")}"
        }
    }
}