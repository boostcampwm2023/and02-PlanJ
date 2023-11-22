package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(category: List<Category>)

    @Query("SELECT categoryName FROM categories")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT * FROM categories")
    fun getAllCategory(): Flow<List<Category>>


    @Query("SELECT * FROM schedules WHERE categoryTitle = :title")
    suspend fun getCategoryTitleSchedule(title : String) : List<Schedule>

    @Delete
    fun deleteCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

}