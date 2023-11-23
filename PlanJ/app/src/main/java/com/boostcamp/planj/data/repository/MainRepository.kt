package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.PostCategoryBody
import com.boostcamp.planj.data.model.PostCategoryResponse
import com.boostcamp.planj.data.model.PostScheduleBody
import com.boostcamp.planj.data.model.PostScheduleResponse
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getSchedules(): Flow<List<Schedule>>

    suspend fun insertSchedule(schedule: Schedule)

    suspend fun deleteSchedule(schedule: Schedule)

    suspend fun deleteScheduleUsingId(id: String)

    fun getCategories(): Flow<List<String>>

    fun getAllCategories(): Flow<List<Category>>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)

    fun getCategoryTitleSchedule(title: String): Flow<List<Schedule>>

    suspend fun insertUser(email: String)

    suspend fun deleteUser(email: String)

    fun getAllUser(): Flow<List<User>>

    fun postCategory(postCategoryBody: PostCategoryBody) : Flow<PostCategoryResponse>

    fun postSchedule(userId : String, categoryId : String, title: String ,endTime : String) : Flow<PostScheduleResponse>
    fun getUser() : Flow<String>

    fun getCategory(categoryName : String) : Category

}