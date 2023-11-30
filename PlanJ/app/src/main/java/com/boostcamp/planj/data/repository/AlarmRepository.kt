package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.AlarmInfo

interface AlarmRepository {

    suspend fun setAlarm(alarmInfo: AlarmInfo)

    suspend fun setAllAlarm()

    fun deleteAlarm(requestCode: Int)

    fun deleteAllAlarm()
}