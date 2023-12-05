package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import com.boostcamp.planj.data.model.dto.CategoryResponse
import com.boostcamp.planj.data.model.dto.GetScheduleCheckedResponse
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

    fun postCategory(postCategoryBody: PostCategoryBody): Flow<PostCategoryResponse>

    fun postSchedule(categoryId: String, title: String, endTime: DateTime): Flow<PostScheduleResponse>

    suspend fun emptyToken()

    suspend fun deleteScheduleApi(scheduleUuid: String)

    suspend fun deleteCategoryApi(categoryUuid: String) : Flow<CategoryResponse>

    fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse>

    suspend fun updateCategoryApi(
        categoryUuid: String,
        categoryName: String
    ): Flow<CategoryResponse>

    fun getCategoryListApi(): Flow<List<Category>>

    suspend fun getCategorySchedulesApi(categoryUuid: String): Flow<GetSchedulesResponse>

    suspend fun getWeeklyScheduleApi(date: String): Flow<GetSchedulesResponse>

    suspend fun getDailyScheduleApi(date: String): Flow<List<Schedule>>

    suspend fun postFriendApi(friendEmail: String)

    suspend fun getFriendsApi(): Flow<List<User>>

    suspend fun deleteAccount()

    suspend fun getMyInfo(): Flow<User>

    fun patchUser(nickName : String, imageFile : MultipartBody.Part?) : Flow<PostUserResponse>

    suspend fun getDetailSchedule(scheduleId: String): ScheduleDetail

    fun getScheduleChecked(scheduleId: String):Flow<GetScheduleCheckedResponse>

    suspend fun getUserImageRemove()
}