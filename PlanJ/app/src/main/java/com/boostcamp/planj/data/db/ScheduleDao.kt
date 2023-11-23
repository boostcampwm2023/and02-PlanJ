package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedules")

    fun getSchedules(): Flow<List<Schedule>>

    @Query("SELECT categoryTitle FROM schedules")
    fun getCategories(): Flow<List<String>>

    @Query(
        "SELECT * FROM schedules " +/*
                "WHERE ((endTime BETWEEN :start And :end ) " +
                "OR (startTime BETWEEN :start AND :end) " +
                "OR (startTime is null AND endTime BETWEEN :start And :end ))" +*/
                "ORDER BY startTime ASC"
    )
    fun getWeekSchedule(): Flow<List<Schedule>>

}