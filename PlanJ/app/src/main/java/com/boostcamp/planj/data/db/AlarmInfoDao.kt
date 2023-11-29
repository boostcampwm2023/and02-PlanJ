package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Insert
import com.boostcamp.planj.data.model.AlarmInfo

@Dao
interface AlarmInfoDao {

    @Insert
    fun insertAlarmInfo(alarmInfo: AlarmInfo)
}