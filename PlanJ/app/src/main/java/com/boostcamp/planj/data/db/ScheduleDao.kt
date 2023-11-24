package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Query("DELETE From schedules WHERE scheduleId == :id")
    suspend fun deleteScheduleUsingId(id: String)

    @Query("DELETE FROM schedules WHERE categoryTitle = :categoryName")
    suspend fun deleteScheduleUsingCategory(categoryName : String)

    @Query("UPDATE schedules SET categoryTitle = :categoryNameAfter WHERE categoryTitle = :categoryNameBefore")
    suspend fun updateScheduleUsingCategory(categoryNameBefore : String, categoryNameAfter : String)

    @Query("SELECT * FROM schedules")
    fun getSchedules(): Flow<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE title LIKE '%' || :input || '%'")
    fun searchSchedule(input: String): Flow<List<Schedule>>
  
    @Query("SELECT categoryTitle FROM schedules")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT * FROM schedules WHERE categoryTitle = :title")
    fun getCategoryTitleSchedule(title : String) : Flow<List<Schedule>>
}