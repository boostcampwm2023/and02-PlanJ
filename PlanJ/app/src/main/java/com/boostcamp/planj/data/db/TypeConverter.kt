package com.boostcamp.planj.data.db

import androidx.room.TypeConverter
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Location
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
    fun fromLocationList(value: Location?): String? = Gson().toJson(value)

    @TypeConverter
    fun toLocationList(value: String?): Location? =
        Gson().fromJson(value, Location::class.java)

    @TypeConverter
    fun fromUserList(value: List<User>?): String? = Gson().toJson(value)

    @TypeConverter
    fun toUserList(value: String?): List<User>? =
        Gson().fromJson(value, Array<User>::class.java)?.toList()

    @TypeConverter
    fun fromDateTimeList(value: DateTime?): String? = Gson().toJson(value)

    @TypeConverter
    fun toDateTimeList(value: String?): DateTime? =
        Gson().fromJson(value, DateTime::class.java)
}