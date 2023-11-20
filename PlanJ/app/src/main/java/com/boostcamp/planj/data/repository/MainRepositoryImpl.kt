package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.db.AppDatabase
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : MainRepository {

    override suspend fun insertSchedule(schedule: Schedule) {
        db.scheduleDao().insertSchedule(schedule)
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        db.scheduleDao().deleteSchedule(schedule)
    }

    override fun getSchedules(): Flow<List<Schedule>> {
        return db.scheduleDao().getSchedules()
    }

    override fun getCategories(): Flow<List<String>> {
        return db.scheduleDao().getCategories()
    }
}