package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.boostcamp.planj.data.model.AlarmInfo

@Dao
interface AlarmInfoDao {

    @Query("DELETE FROM alarms")
    suspend fun deleteAllAlarmInfo()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmInfo(alarmInfo: AlarmInfo)

    @Delete
    suspend fun deleteAlarmInfo(alarmInfo: AlarmInfo)

    @Query("DELETE FROM alarms WHERE scheduleId = :scheduleId")
    suspend fun deleteAlarmInfoUsingScheduleId(scheduleId: String)

    @Query("SELECT * FROM alarms")
    suspend fun getAll(): List<AlarmInfo>

    @Update
    suspend fun updateAlarmInfo(alarmInfo: AlarmInfo)
}