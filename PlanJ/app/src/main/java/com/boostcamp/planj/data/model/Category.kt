package com.boostcamp.planj.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Category(
    @SerializedName("categoryUuid") val categoryUuid: String,
    @SerializedName("categoryName") val categoryName: String
)
