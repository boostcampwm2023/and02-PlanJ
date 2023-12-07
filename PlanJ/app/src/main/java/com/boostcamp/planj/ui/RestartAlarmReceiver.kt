package com.boostcamp.planj.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.boostcamp.planj.data.repository.LoginRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RestartAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var loginRepository: LoginRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val alarm = PlanjAlarm(context)
            CoroutineScope(Dispatchers.IO).launch {
                loginRepository.updateAlarmInfo(System.currentTimeMillis())
                val alarmList = loginRepository.getAllAlarmInfo()
                alarmList.forEach { alarmInfo -> alarm.setAlarm(alarmInfo) }
            }
        }
    }
}