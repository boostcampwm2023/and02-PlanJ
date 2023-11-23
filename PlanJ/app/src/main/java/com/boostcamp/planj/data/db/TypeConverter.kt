package com.boostcamp.planj.data.db

import androidx.room.TypeConverter
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.User
import com.google.gson.Gson

class TypeConverter {

    @TypeConverter
    fun fromRepetitionList(value: Repetition?): String? = Gson().toJson(value)

    @TypeConverter
    fun toRepetitionList(value: String?): Repetition? =
        Gson().fromJson(value, Repetition::class.java)

    @TypeConverter
    fun fromAlarmList(value: Alarm?): String? = Gson().toJson(value)

    @TypeConverter
    fun toAlarmList(value: String?): Alarm? =
        Gson().fromJson(value, Alarm::class.java)

    @TypeConverter
    fun fromUserList(value: List<User>?): String? = Gson().toJson(value)

    @TypeConverter
    fun toUserList(value: String?): List<User>? =
        Gson().fromJson(value, Array<User>::class.java)?.toList()
}