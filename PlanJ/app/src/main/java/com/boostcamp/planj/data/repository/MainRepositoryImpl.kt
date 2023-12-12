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
import java.util.Calendar
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
        val scheduleDetail = api.getDetailSchedule(scheduleId).scheduleDetail

        val startAt =
            scheduleDetail.startAt?.split("T", "-", ":")?.map { time -> time.toInt() }
                ?: emptyList()
        val endAt = scheduleDetail.endAt.split("T", "-", ":").map { time -> time.toInt() }

        val schedule = Schedule(
            scheduleId = scheduleDetail.scheduleUuid,
            categoryName = scheduleDetail.categoryName,
            title = scheduleDetail.title,
            description = scheduleDetail.description,
            startAt = if (startAt.isEmpty()) null else DateTime(
                startAt[0],
                startAt[1],
                startAt[2],
                startAt[3],
                startAt[4],
                startAt[5]
            ),
            endAt = DateTime(endAt[0], endAt[1], endAt[2], endAt[3], endAt[4], endAt[5]),
            startLocation = scheduleDetail.startLocation,
            endLocation = scheduleDetail.endLocation,
            repetition = scheduleDetail.repetition,
            participants = scheduleDetail.participants,
            alarm = scheduleDetail.alarm
        )
        emit(schedule)
    }

    override suspend fun getUserImageRemove() {
        return api.patchUserImageRemove()
    }

    override fun getSearchSchedules(keyword: String): Flow<List<Schedule>> = flow {
        val scheduleInfo = api.getSearchSchedules(keyword)
        val scheduleDummy = scheduleInfo.data.map {

            val startAt =
                it.startAt?.split("T", "-", ":")?.map { time -> time.toInt() } ?: emptyList()
            val endAt = it.endAt.split("T", "-", ":").map { time -> time.toInt() }

            Schedule(
                scheduleId = it.scheduleUuid,
                title = it.title,
                startAt = if (startAt.isEmpty()) null else DateTime(
                    startAt[0],
                    startAt[1],
                    startAt[2],
                    startAt[3],
                    startAt[4],
                    startAt[5]
                ),
                endAt = DateTime(endAt[0], endAt[1], endAt[2], endAt[3], endAt[4], endAt[5]),
                isFinished = it.isFinished,
                isFailed = it.isFailed,
                repeated = it.repeated,
                hasRetrospectiveMemo = it.hasRetrospectiveMemo,
                shared = it.shared,
                participantCount = it.participantCount,
                participantSuccessCount = it.participantSuccessCount
            )
        }
        emit(scheduleDummy)
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
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = alarmInfo.endTime.toMilliseconds()
                calendar.add(Calendar.MINUTE, -alarmInfo.alarmTime - alarmInfo.estimatedTime)
                calendar.timeInMillis > curMillis
            }
            emit(alarmInfo)
        } catch (e: Exception) {
            Log.d("PLANJDEBUG", "getAlarms error ${e.message}")
        }
    }
}


