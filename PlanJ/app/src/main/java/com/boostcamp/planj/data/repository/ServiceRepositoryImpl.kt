package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.ScheduleInfo
import com.boostcamp.planj.data.network.ServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api : ServiceApi
) : ServiceRepository {

    override fun getDailySchedule(date: String): Flow<List<ScheduleInfo>> = flow {
        val scheduleInfo = api.getDailySchedule(date)
        emit(scheduleInfo.data)
    }
}