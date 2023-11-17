package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getSchedules() : Flow<List<Schedule>>

    suspend fun insertSchedule(schedule: Schedule)

    suspend fun deleteSchedule(schedule: Schedule)

    fun getCategories(): Flow<List<String>>

    suspend fun insertCategory(category: Category)
}