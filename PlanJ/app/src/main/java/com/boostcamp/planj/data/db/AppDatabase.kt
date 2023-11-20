package com.boostcamp.planj.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User


@Database(entities = [User::class, Schedule::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun scheduleDao(): ScheduleDao
}