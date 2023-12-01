package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.GetCategoryResponse
import com.boostcamp.planj.data.model.dto.GetFriendResponse
import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import com.boostcamp.planj.data.model.dto.PatchCategoryResponse
import com.boostcamp.planj.data.model.dto.PatchScheduleBody
import com.boostcamp.planj.data.model.dto.PatchScheduleResponse
import com.boostcamp.planj.data.model.dto.PostCategoryBody
import com.boostcamp.planj.data.model.dto.PostCategoryResponse
import com.boostcamp.planj.data.model.dto.PostScheduleResponse
import com.boostcamp.planj.data.model.dto.PostUserResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

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

    fun getWeekSchedule(): Flow<List<Schedule>>

    fun getCategoryTitleSchedule(title: String): Flow<List<Schedule>>

    suspend fun insertUser(email: String)

    suspend fun deleteUser(email: String)

    fun getAllUser(): Flow<List<User>>

    fun searchSchedule(input: String): Flow<List<Schedule>>

    fun postCategory(postCategoryBody: PostCategoryBody): Flow<PostCategoryResponse>

    fun postSchedule(categoryId: String, title: String, endTime: String): Flow<PostScheduleResponse>

    fun getToken(): Flow<String>

    suspend fun emptyToken()

    fun getCategory(categoryName: String): Category

    suspend fun deleteScheduleApi(scheduleUuid: String)

    suspend fun deleteCategoryApi(categoryUuid: String)

    suspend fun updateSchedule(schedule: Schedule)

    suspend fun updateScheduleUsingCategory(categoryNameBefore: String, categoryAfter: String)

    fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse>

    suspend fun deleteScheduleUsingCategoryName(categoryName: String)

    suspend fun updateCategoryApi(
        categoryUuid: String,
        categoryName: String
    ): Flow<PatchCategoryResponse>

    suspend fun getCategoryListApi(): Flow<GetCategoryResponse>

    suspend fun getCategorySchedulesApi(categoryUuid: String): Flow<GetSchedulesResponse>

    suspend fun getWeeklyScheduleApi(date: String): Flow<GetSchedulesResponse>

    suspend fun getDailyScheduleApi(date: String): Flow<GetSchedulesResponse>

    suspend fun postFriendApi(friendEmail: String)

    suspend fun getFriendsApi(): Flow<List<User>>

    suspend fun deleteAccount()

    suspend fun getMyInfo(): Flow<User>

    fun patchUser(nickName : String, imageFile : MultipartBody.Part?) : Flow<PostUserResponse>

    suspend fun saveAlarmMode(mode: Boolean)
    suspend fun getAlarmMode(): Flow<Boolean>

    suspend fun deleteAllData()
}