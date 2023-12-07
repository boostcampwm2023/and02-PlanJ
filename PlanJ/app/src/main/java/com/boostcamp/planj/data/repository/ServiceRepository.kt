package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.ScheduleInfo
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {

    fun getDailySchedule(date : String) : Flow<List<ScheduleInfo>>
}