package com.boostcamp.planj.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
    val imgUrl: String? = null,
    val nickname: String,
    @PrimaryKey val email: String
) : Parcelable
