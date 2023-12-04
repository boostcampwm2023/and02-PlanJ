package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.GetCategoryResponse
import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import com.boostcamp.planj.data.model.dto.PatchCategoryResponse
import com.boostcamp.planj.data.model.dto.PatchScheduleBody
import com.boostcamp.planj.data.model.dto.PatchScheduleResponse
import com.boostcamp.planj.data.model.dto.PostCategoryBody
import com.boostcamp.planj.data.model.dto.PostCategoryResponse
import com.boostcamp.planj.data.model.dto.PostScheduleResponse
import com.boostcamp.planj.data.model.dto.PostUserResponse
import com.boostcamp.planj.data.model.dto.ScheduleDetail
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface MainRepository {

    fun getCategories(): Flow<List<String>>

    fun getAllCategories(): Flow<List<Category>>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)

    fun postCategory(postCategoryBody: PostCategoryBody): Flow<PostCategoryResponse>

    fun postSchedule(categoryId: String, title: String, endTime: DateTime): Flow<PostScheduleResponse>

    fun getToken(): Flow<String>

    suspend fun emptyToken()

    fun getCategory(categoryName: String): Category

    suspend fun deleteScheduleApi(scheduleUuid: String)

    suspend fun deleteCategoryApi(categoryUuid: String)

    fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse>

    suspend fun updateCategoryApi(
        categoryUuid: String,
        categoryName: String
    ): Flow<PatchCategoryResponse>

    suspend fun getCategoryListApi(): Flow<GetCategoryResponse>

    suspend fun getCategorySchedulesApi(categoryUuid: String): Flow<GetSchedulesResponse>

    suspend fun getWeeklyScheduleApi(date: String): Flow<GetSchedulesResponse>

    suspend fun getDailyScheduleApi(date: String): Flow<List<Schedule>>

    suspend fun postFriendApi(friendEmail: String)

    suspend fun getFriendsApi(): Flow<List<User>>

    suspend fun deleteAccount()

    suspend fun getMyInfo(): Flow<User>

    fun patchUser(nickName : String, imageFile : MultipartBody.Part?) : Flow<PostUserResponse>

    suspend fun saveAlarmMode(mode: Boolean)

    suspend fun getAlarmMode(): Flow<Boolean>

    suspend fun deleteAllData()

    suspend fun insertAlarmInfo(alarmInfo: AlarmInfo)

    suspend fun getAllAlarmInfo(): List<AlarmInfo>

    suspend fun deleteAlarmInfo(alarmInfo: AlarmInfo)

    suspend fun deleteAlarmInfoUsingScheduleId(scheduleId: String)

    suspend fun updateAlarmInfo(curTimeMillis: Long)

    suspend fun getDetailSchedule(scheduleId: String): ScheduleDetail
}