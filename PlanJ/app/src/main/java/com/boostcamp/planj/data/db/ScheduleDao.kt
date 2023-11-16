package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Insert
import com.boostcamp.planj.data.model.Schedule

@Dao
interface ScheduleDao {

    @Insert
    fun insertSchedule(schedule: Schedule)
}