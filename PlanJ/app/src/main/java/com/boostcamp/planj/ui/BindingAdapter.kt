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
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.naver.NaverResponse
import com.boostcamp.planj.ui.login.EmailState
import com.boostcamp.planj.ui.login.PwdState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

@BindingAdapter("dateTimeInfo")
fun TextView.setDateTimeInfo(dateTime: DateTime?) {
    text = if (dateTime == null) {
        resources.getString(R.string.not_set)
    } else {
        String.format(
            "%04d/%02d/%02d %02d:%02d",
            dateTime.year,
            dateTime.month,
            dateTime.day,
            dateTime.hour,
            dateTime.minute
        )
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
    } else if (repetition.cycleType == "DAILY") {
        resources.getString(R.string.repeat_per_day, repetition.cycleCount)
    } else {
        resources.getString(R.string.repeat_per_week, repetition.cycleCount)
    }
}

@BindingAdapter("alarmInfo")
fun TextView.setAlarmInfo(alarmInfo: Alarm?) {
    text = if (alarmInfo != null && alarmInfo.alarmType == "DEPARTURE") {
        resources.getString(R.string.before_departure_time, alarmInfo.alarmTime)
    } else if (alarmInfo != null && alarmInfo.alarmType == "END") {
        resources.getString(R.string.before_end_time, alarmInfo.alarmTime)
    } else {
        resources.getString(R.string.not_set)
    }
}

@BindingAdapter("userImg")
fun ImageView.setImage(url: String?) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.ic_circle_person)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

@BindingAdapter("participantsNum")
fun TextView.setParticipantsNum(participants: List<Participant>) {
    val successNum = participants.filter { it.isFinished }.size
    text = "$successNum / ${participants.size}"
}

@BindingAdapter("participation")
fun TextView.setParticipation(schedule: Schedule) {
    if (schedule.participantCount < 2) {
        visibility = View.GONE
        return
    }
    text = "${schedule.participantSuccessCount ?: 0} / ${schedule.participantCount}"
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
    if (item.categoryUuid == "all")
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
        SimpleDateFormat("yyyy/MM/dd", Locale("kr", "ko")).format(System.currentTimeMillis())
    val scheduleEndTime = schedule.endAt
    val endDate = String.format(
        "%04d/%02d/%02d",
        scheduleEndTime.year,
        scheduleEndTime.month,
        scheduleEndTime.day
    )
    val endTime = String.format("%02d:%02d", scheduleEndTime.hour, scheduleEndTime.minute)
    if (schedule.startAt == null) {
        text = if (currentDate == endDate) {
            "오늘 $endTime 까지"
        } else {
            "$endDate $endTime"
        }
    } else {
        val scheduleStartTime = schedule.startAt
        val startDate = String.format(
            "%04d/%02d/%02d",
            scheduleStartTime.year,
            scheduleStartTime.month,
            scheduleStartTime.day
        )
        val startTime = String.format("%02d:%02d", scheduleStartTime.hour, scheduleStartTime.minute)
        text = if (startDate == endDate) {
            if (currentDate == startDate) {
                "오늘 $startTime ~ $endTime"
            } else {
                "$startDate $startTime ~ $endTime"
            }
        } else {
            "$startDate - $endDate"
        }
    }
}


@BindingAdapter("failedTime")
fun TextView.failedTime(schedule: Schedule) {
    val start = if(schedule.startAt == null) null else String.format(
        "%04d-%02d-%02d %02d:%02d",
        schedule.startAt.year,
        schedule.startAt.month,
        schedule.startAt.day,
        schedule.startAt.hour,
        schedule.startAt.minute
    )

    val end = String.format(
        "%04d-%02d-%02d %02d:%02d",
        schedule.endAt.year,
        schedule.endAt.month,
        schedule.endAt.day,
        schedule.endAt.hour,
        schedule.endAt.minute
    )

    text = if(start == null){
        end
    }else{
        "$start - $end"
    }
}


@BindingAdapter("setTime")
fun TextView.setTime(response: NaverResponse?) {
    response?.let {
        var time = it.route.trafast[0].summary.duration
        val hour = time / (1000 * 60 * 60)
        time %= (1000 * 60 * 60)
        val min = time / (1000 * 60)

        text = if (hour == 0) {
            "소요 시간 ${min}분"
        } else {
            "소요 시간 ${hour}시 ${min}분"
        }
    }
}

@BindingAdapter("setDistance")
fun TextView.setDistance(response: NaverResponse?) {
    response?.let {
        val distance = it.route.trafast[0].summary.distance

        text = if (distance >= 1000) {
            "거리 : ${distance / 1000.0}km"
        } else {
            "거리 : ${distance}m "
        }

    }
}