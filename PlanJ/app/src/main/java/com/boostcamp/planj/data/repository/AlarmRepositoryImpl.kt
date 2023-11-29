package com.boostcamp.planj.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.boostcamp.planj.data.PlanJReceiver
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.Schedule
import java.util.Calendar
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val context: Context
) : AlarmRepository {

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val alarmIntent by lazy {
        Intent(context, PlanJReceiver::class.java)
    }

    override fun setAlarm(schedule: Schedule) {
        schedule.alarm?.let { alarm ->
            val requestCode = schedule.scheduleId.hashCode()
            val title = schedule.title
            val text = if (alarm.alarmType == "DEPARTURE") {
                "출발 시간 ${alarm.alarmTime}분 전입니다."
            } else {
                "종료 시간 ${alarm.alarmTime}분 전입니다."
            }
            val pendingIntent = createPendingIntent(requestCode, title, text)

            val dateList = schedule.endTime.split("T", "/", ":")
            val calendar = Calendar.getInstance()
            calendar.set(
                dateList[0].toInt(),
                dateList[1].toInt() - 1,
                dateList[2].toInt(),
                dateList[3].toInt(),
                dateList[4].toInt()
            )
            calendar.add(Calendar.MINUTE, -alarm.alarmTime)

            if (schedule.repetition == null) {
                setOnTimeAlarm(calendar, pendingIntent)
            } else {
                setRepeatAlarm(calendar, schedule.repetition, pendingIntent)
            }
        }
    }

    private fun setOnTimeAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
        alarmManager.set(
            AlarmManager.RTC,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun setRepeatAlarm(
        calendar: Calendar,
        repetition: Repetition,
        pendingIntent: PendingIntent
    ) {
        val interval = if (repetition.cycleType == "DAILY") {
            repetition.cycleCount
        } else {
            repetition.cycleCount * 7
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * interval,
            pendingIntent
        )
    }

    override fun deleteAlarm(requestCode: Int) {
        alarmManager.cancel(createPendingIntent(requestCode))
    }

    private fun createPendingIntent(
        requestCode: Int,
        title: String = "",
        text: String = ""
    ): PendingIntent {
        return alarmIntent.let { intent ->
            intent.putExtra("title", title)
            intent.putExtra("text", text)
            PendingIntent.getBroadcast(
                context,
                requestCode,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}