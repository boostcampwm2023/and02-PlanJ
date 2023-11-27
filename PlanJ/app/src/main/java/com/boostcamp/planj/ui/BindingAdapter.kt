package com.boostcamp.planj.ui

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.naver.NaverResponse
import com.boostcamp.planj.getDate
import com.boostcamp.planj.getTime
import com.boostcamp.planj.ui.login.EmailState
import com.boostcamp.planj.ui.login.PwdState
import com.boostcamp.planj.ui.schedule.ScheduleStartMapViewModel
import com.boostcamp.planj.ui.schedule.ScheduleViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Locale

@BindingAdapter("emailError")
fun TextInputLayout.setEmailError(emailState: EmailState) {
    error = when (emailState) {
        EmailState.ERROR_FORMAT -> resources.getString(R.string.email_format_error)
        EmailState.ERROR_EXIST -> resources.getString(R.string.email_exist_error)
        else -> null
    }
}

@BindingAdapter("pwdError")
fun TextInputLayout.setPwdError(pwdState: PwdState) {
    error = when (pwdState) {
        PwdState.ERROR_LENGTH -> resources.getString(R.string.pwd_length_error)
        PwdState.ERROR_CONTENTS -> resources.getString(R.string.pwd_contents_error)
        else -> null
    }
}

@BindingAdapter("memoLength")
fun TextView.setMemoLength(memo: String?) {
    text = resources.getString(R.string.memo_length, memo?.length ?: 0)
}

@BindingAdapter("repetitionInfo")
fun TextView.setRepetitionInfo(repetition: Repetition?) {
    text = if (repetition == null) {
        resources.getString(R.string.not_set)
    } else if (repetition.cycleType == "daily") {
        resources.getString(R.string.repeat_per_day, repetition.cycleCount)
    } else {
        resources.getString(R.string.repeat_per_week, repetition.cycleCount)
    }
}

@BindingAdapter("alarmInfo")
fun TextView.setAlarmInfo(alarmInfo: Alarm?) {
    text = if (alarmInfo != null && alarmInfo.alarmType == "departure") {
        resources.getString(R.string.before_departure_time, alarmInfo.alarmTime)
    } else if (alarmInfo != null && alarmInfo.alarmType == "end") {
        resources.getString(R.string.before_end_time, alarmInfo.alarmTime)
    } else {
        resources.getString(R.string.not_set)
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
    paintFlags = if (schedule.isFinished) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
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

@BindingAdapter("setDateTime")
fun TextView.setDateTime(schedule: Schedule) {
    val currentDate =
        SimpleDateFormat("yyyy-MM-dd", Locale("kr", "ko")).format(System.currentTimeMillis())
    val endDate = schedule.endTime.getDate()
    val endTime = schedule.endTime.getTime().split(":").subList(0, 2).joinToString(":")
    if (schedule.startTime == null) {

        text = if (currentDate == endDate) {
            "오늘 $endTime 까지"
        } else {
            "${endDate.replace("-", "/")} $endTime"
        }
    } else {
        val startDate = schedule.startTime.getDate()
        val startTime = schedule.startTime.getTime().split(":").subList(0, 2).joinToString(":")
        text = if (startDate == endDate) {
            if (currentDate == startDate) {
                "오늘 $startTime ~ $endTime"
            } else {
                "${startDate.replace("-", "/")} $startTime ~ $endTime"
            }
        } else {
            "${startDate.replace("-", "/")} - ${endDate.replace("-", "/")}"
        }
    }
}

@BindingAdapter("endImageVisible")
fun ImageView.setImageVisible(viewModel : ScheduleViewModel){
    if(!viewModel.isEditMode.value) {
        visibility = View.GONE
        return
    }

    visibility =if(viewModel.scheduleLocation.value == null) {
         View.GONE
    }else{
        View.VISIBLE
    }
}

@BindingAdapter("startImageVisible")
fun ImageView.setEndImageVisible(viewModel : ScheduleViewModel){
    if(!viewModel.isEditMode.value) {
        visibility = View.GONE
        return
    }

    visibility =if(viewModel.startScheduleLocation.value == null) {
        View.GONE
    }else{
        View.VISIBLE
    }
}

@BindingAdapter("btnVisible")
fun TextView.setVisible(viewModel : ScheduleViewModel){
    visibility =if(viewModel.startScheduleLocation.value == null || viewModel.scheduleLocation.value == null) {
        View.GONE
    }else{
        View.VISIBLE
    }
}

@BindingAdapter("setTime")
fun TextView.setTime(response: NaverResponse?){
    response?.let {
        var time = it.route.trafast[0].summary.duration
        val hour = time/(1000 * 60 * 60)
        time %= (1000 * 60 * 60)
        val min = time/(1000 * 60)

        text = if(hour == 0){
            "소요 시간 ${min}분"
        } else {
            "소요 시간 ${hour}시 ${min}분"
        }
    }
}

@BindingAdapter("setDistance")
fun TextView.setDistance(response: NaverResponse?){
    response?.let {
        val distance = it.route.trafast[0].summary.distance

        text = if(distance >= 1000){
            "거리 : ${distance/1000.0}km"
        } else {
            "거리 : ${distance}m "
        }

    }
}