package com.boostcamp.planj.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.boostcamp.planj.data.PlanJReceiver
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

    override fun setAlarm() {
        TODO("Not yet implemented")
    }

    override fun deleteAlarm(requestCode: Int) {
        alarmManager.cancel(createPendingIntent(requestCode))
    }

    private fun createPendingIntent(requestCode: Int): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}