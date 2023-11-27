package com.boostcamp.planj.data.repository

interface AlarmRepository {

    fun setAlarm()

    fun deleteAlarm(requestCode: Int)
}