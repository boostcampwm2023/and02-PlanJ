package com.boostcamp.planj.data.db

import androidx.room.TypeConverter
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.User
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TypeConverter {

    @TypeConverter
    fun fromRepetitionList(value: Repetition? ) : String? = Gson().toJson(value)

    @TypeConverter
    fun toRepetitionList(value: String?) : Repetition? = Gson().fromJson(value, Repetition::class.java)

    @TypeConverter
    fun fromUserList(value: List<User>? ) : String? = Gson().toJson(value)

    @TypeConverter
    fun toUserList(value: String?) : List<User>? = Gson().fromJson(value, Array<User>::class.java).toList()
}