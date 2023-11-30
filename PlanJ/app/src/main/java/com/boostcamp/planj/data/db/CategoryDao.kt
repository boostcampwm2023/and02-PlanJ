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

    @Query("SELECT categoryName FROM categories WHERE categoryName != '전체 일정'")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT * FROM categories")
    fun getAllCategory(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE categoryName = :categoryName")
    fun getCategory(categoryName : String) : Category

    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("DELETE FROM categories")
    fun deleteAllCategory()

}