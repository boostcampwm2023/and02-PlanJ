package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow


interface MainRepository {

    fun getSchedules() : Flow<List<Schedule>>

    fun insertSchedule(schedule: Schedule)
}