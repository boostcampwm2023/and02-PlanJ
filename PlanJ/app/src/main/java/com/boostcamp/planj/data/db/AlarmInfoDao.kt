package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.boostcamp.planj.data.model.AlarmInfo

@Dao
interface AlarmInfoDao {

    @Insert
    fun insertAlarmInfo(alarmInfo: AlarmInfo)

    @Query("DELETE FROM AlarmInfo")
    suspend fun deleteAllAlarmInfo()
}