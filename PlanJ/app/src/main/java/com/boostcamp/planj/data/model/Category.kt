package com.boostcamp.planj.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val categoryId: String,
    val categoryName: String
)
