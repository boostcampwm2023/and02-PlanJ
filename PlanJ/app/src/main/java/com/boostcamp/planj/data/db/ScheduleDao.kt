package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert
    fun insertSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedules")
    fun getSchedules() : Flow<List<Schedule>>
}