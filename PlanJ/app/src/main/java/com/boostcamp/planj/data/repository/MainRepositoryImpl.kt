package com.boostcamp.planj.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.FailedMemo
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.CategoryResponse
import com.boostcamp.planj.data.model.dto.DeleteFriendBody
import com.boostcamp.planj.data.model.dto.DeleteScheduleBody
import com.boostcamp.planj.data.model.dto.GetScheduleCheckedResponse
import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import com.boostcamp.planj.data.model.dto.PatchScheduleBody
import com.boostcamp.planj.data.model.dto.PatchScheduleResponse
import com.boostcamp.planj.data.model.dto.PostCategoryBody
import com.boostcamp.planj.data.model.dto.PostCategoryResponse
import com.boostcamp.planj.data.model.dto.PostFriendRequest
import com.boostcamp.planj.data.model.dto.PostScheduleAddMemoBody
import com.boostcamp.planj.data.model.dto.PostScheduleBody
import com.boostcamp.planj.data.model.dto.PostScheduleResponse
import com.boostcamp.planj.data.model.dto.PostUserResponse
import com.boostcamp.planj.data.network.MainApi
import com.boostcamp.planj.data.getDateTime
import com.boostcamp.planj.data.scheduleReformat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi,
    private val dataStore: DataStore<Preferences>,
) : MainRepository {

    override fun postCategory(postCategoryBody: PostCategoryBody): Flow<PostCategoryResponse> =
        flow {
            emit(api.postCategory(postCategoryBody))
        }

    override fun postSchedule(
        categoryId: String,
        title: String,
        endTime: DateTime
    ): Flow<PostScheduleResponse> = flow {
        val postSchedule = PostScheduleBody(categoryId, title, endTime.toFormattedString())
        emit(api.postSchedule(postSchedule))
    }

    override suspend fun emptyToken() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    override suspend fun deleteScheduleApi(scheduleUuid: String) {
        api.deleteSchedule(DeleteScheduleBody(scheduleUuid))
    }

    override fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse> =
        flow {
            emit(api.patchSchedule(patchScheduleBody))
        }

    override suspend fun deleteCategoryApi(categoryUuid: String): Flow<CategoryResponse> = flow {
        emit(api.deleteCategory(categoryUuid))
    }

    override suspend fun updateCategoryApi(
        categoryUuid: String,
        categoryName: String
    ): Flow<CategoryResponse> = flow {
        emit(api.patchCategory(Category(categoryUuid, categoryName)))
    }

    override fun getCategoryListApi(): Flow<List<Category>> = flow {
        emit(api.getCategoryList().data)
    }

    override suspend fun getCategorySchedulesApi(categoryUuid: String): Flow<List<Schedule>> =
        flow {
            try {
                val scheduleInfo = api.getCategorySchedule(categoryUuid)
                val scheduleReformat = scheduleInfo.data.map {
                    scheduleReformat(it)
                }

                emit(scheduleReformat)

            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "getCategorySchedulesApi error ${e.message}")
                throw e
            }
        }

    override suspend fun getWeeklyScheduleApi(date: String): Flow<GetSchedulesResponse> = flow {
        emit(api.getWeeklySchedule(date))
    }

    override suspend fun getDailyScheduleApi(date: String): Flow<List<Schedule>> = flow {
        try {
            val scheduleInfo = api.getDailySchedule(date)
            val scheduleReformat = scheduleInfo.data.map {
                Log.d("PLANJDEBUG", "getDailyScheduleApi success ${it.title} - ${it.isAuthor}")
                scheduleReformat(it)
            }
            emit(scheduleReformat)
        } catch (e: Exception) {
            Log.d("PLANJDEBUG", "getDailyScheduleApi error  ${e.message}")
        }
    }

    override suspend fun postFriendApi(friendEmail: String) {
        api.postFriend(PostFriendRequest(friendEmail))
    }

    override suspend fun getFriendsApi(): Flow<List<User>> = flow {
        emit(api.getFriends().data)
    }

    override suspend fun deleteFriendApi(email: DeleteFriendBody) {
        try {
            api.deleteFriends(email)
        } catch (e: Exception) {
            Log.d("PLANJDEBUG", "deleteFriendApi ${e.message}")
        }
    }

    override suspend fun deleteAccount() {
        api.deleteAccount()
    }

    override suspend fun getMyInfo(): Flow<User> = flow {
        emit(api.getMyInfo().data)
    }

    override fun patchUser(
        nickName: String,
        imageFile: MultipartBody.Part?
    ): Flow<PostUserResponse> = flow {
        emit(api.patchUser(nickName, imageFile))
    }

    override fun getScheduleChecked(scheduleId: String): Flow<GetScheduleCheckedResponse> =
        flow { emit(api.getScheduleChecked(scheduleId)) }

    override suspend fun getDetailSchedule(scheduleId: String): Flow<Schedule> = flow {
        try {
            val scheduleDetail = api.getDetailSchedule(scheduleId).scheduleDetail
            val schedule = Schedule(
                scheduleId = scheduleDetail.scheduleUuid,
                categoryName = scheduleDetail.categoryName,
                title = scheduleDetail.title,
                description = scheduleDetail.description,
                startAt = getDateTime(scheduleDetail.startAt),
                endAt = getDateTime(scheduleDetail.endAt) ?: DateTime(0, 0, 0, 0, 0),
                startLocation = scheduleDetail.startLocation,
                endLocation = scheduleDetail.endLocation,
                repetition = scheduleDetail.repetition,
                participants = scheduleDetail.participants,
                alarm = scheduleDetail.alarm
            )
            emit(schedule)
        } catch (e: Exception) {
            Log.d("PLANJDEBUG", "getDetailSchedule error  ${e.message}")
        }
    }

    override suspend fun getUserImageRemove() {
        return api.patchUserImageRemove()
    }

    override fun getSearchSchedules(keyword: String): Flow<List<Schedule>> = flow {
        try {
            val scheduleInfo = api.getSearchSchedules(keyword)
            val scheduleReformat = scheduleInfo.data.map {
                scheduleReformat(it)
            }
            emit(scheduleReformat)
        } catch (e: Exception) {
            Log.d("PLANJDEBUG", "getSearchSchedules error  ${e.message}")
        }
    }

    override suspend fun postScheduleAddMemo(scheduleId: String, memo: String) {
        return api.postScheduleAddMemo(PostScheduleAddMemoBody(scheduleId, memo))
    }

    override fun getFailedMemo(): Flow<List<FailedMemo>> = flow {
        emit(api.getFailedMemo().date)
    }

    override fun getAlarms(): Flow<List<AlarmInfo>> = flow {
        try {
            val curMillis = System.currentTimeMillis()
            val alarms = api.getAlarms().data
            val alarmInfo = alarms.map { alarm ->
                AlarmInfo(
                    scheduleId = alarm.scheduleUuid,
                    title = alarm.title,
                    endTime = getDateTime(alarm.endAt) ?: DateTime(0,0,0,0,0),
                    alarmType = alarm.alarmType,
                    alarmTime = alarm.alarmTime,
                    estimatedTime = alarm.estimatedTime
                )
            }.filter { alarmInfo ->
                alarmInfo.endTime.toMilliseconds() > curMillis
            }
            emit(alarmInfo)
        } catch (e: Exception) {
            Log.d("PLANJDEBUG", "getAlarms error ${e.message}")
        }
    }
}


