package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getSchedules(): Flow<List<Schedule>>

    suspend fun insertSchedule(schedule: Schedule)

    suspend fun deleteSchedule(schedule: Schedule)

    fun getCategories(): Flow<List<String>>

    fun getAllCategories(): Flow<List<Category>>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)
    suspend fun updateCategory(category: Category)

    suspend fun getCategoryTitleSchedule(title: String): List<Schedule>

    fun getWeekSchedule( ): Flow<List<Schedule>>
}