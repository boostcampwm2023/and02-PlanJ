package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Schedule

interface AlarmRepository {

    fun setAlarm(schedule: Schedule)

    fun deleteAlarm(requestCode: Int)
}