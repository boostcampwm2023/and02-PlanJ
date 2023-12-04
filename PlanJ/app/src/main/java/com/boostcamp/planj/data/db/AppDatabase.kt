package com.boostcamp.planj.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [AlarmInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alarmInfoDao(): AlarmInfoDao

}