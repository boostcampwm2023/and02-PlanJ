package com.boostcamp.planj.data.repository


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.data.db.AlarmInfoDao
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.DeleteScheduleBody
import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import com.boostcamp.planj.data.model.dto.CategoryResponse
import com.boostcamp.planj.data.model.dto.PatchScheduleBody
import com.boostcamp.planj.data.model.dto.PatchScheduleResponse
import com.boostcamp.planj.data.model.dto.PostCategoryBody
import com.boostcamp.planj.data.model.dto.PostCategoryResponse
import com.boostcamp.planj.data.model.dto.PostFriendRequest
import com.boostcamp.planj.data.model.dto.PostScheduleBody
import com.boostcamp.planj.data.model.dto.PostScheduleResponse
import com.boostcamp.planj.data.model.dto.PostUserResponse
import com.boostcamp.planj.data.model.dto.ScheduleDetail
import com.boostcamp.planj.data.network.MainApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val api: MainApi,
    private val dataStore: DataStore<Preferences>,
    private val alarmInfoDao: AlarmInfoDao,
) : MainRepository {

    companion object {
        val USER = stringPreferencesKey("User")
        val ALARM_MODE = booleanPreferencesKey("alarm")
    }

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
            prefs[USER] = ""
        }
    }

    override fun getToken(): Flow<String> {
        return dataStore.data
            .catch { e ->
                if (e is IOException) {
                    e.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { pref ->
                pref[USER] ?: ""
            }
    }

    override suspend fun deleteScheduleApi(scheduleUuid: String) {
        api.deleteSchedule(DeleteScheduleBody(scheduleUuid))
    }


    override fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse> =
        flow {
            emit(api.patchSchedule(patchScheduleBody))
        }

    override suspend fun deleteCategoryApi(categoryUuid: String) : Flow<CategoryResponse> = flow {
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

    override suspend fun getCategorySchedulesApi(categoryUuid: String): Flow<GetSchedulesResponse> =
        flow {
            emit(api.getCategorySchedule(categoryUuid))
        }

    override suspend fun getWeeklyScheduleApi(date: String): Flow<GetSchedulesResponse> = flow {
        emit(api.getWeeklySchedule(date))
    }

    override suspend fun getDailyScheduleApi(date: String): Flow<List<Schedule>> = flow {
        try {
            val scheduleInfo = api.getDailySchedule(date)
            val scheduleDummy = scheduleInfo.date.map {

                val startAt = it.startAt?.split("T","-",":")?.map { time -> time.toInt() } ?: emptyList()
                val endAt = it.endAt.split("T","-",":").map { time -> time.toInt() }

                Schedule(
                    scheduleId = it.scheduleUuid,
                    title =  it.title,
                    startAt = if(startAt.isEmpty()) null else DateTime(startAt[0],startAt[1], startAt[2], startAt[3], startAt[4], startAt[5]),
                    endAt = DateTime(endAt[0],endAt[1], endAt[2], endAt[3], endAt[4], endAt[5]),
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
        }catch (e: Exception){
            Log.d("PLANJDEBUG", "getDailyScheduleApi error  ${e.message}")
        }

    }

    override suspend fun postFriendApi(friendEmail: String) {
        api.postFriend(PostFriendRequest(friendEmail))
    }

    override suspend fun getFriendsApi(): Flow<List<User>> = flow {
        emit(api.getFriends().data)
    }

    override suspend fun deleteAccount() {
        api.deleteAccount()
    }

    override suspend fun getMyInfo(): Flow<User> = flow {
        emit(api.getMyInfo().data)
    }

    override fun patchUser(nickName : String, imageFile : MultipartBody.Part?): Flow<PostUserResponse> = flow {
        emit(api.patchUser(nickName, imageFile))
    }

    override suspend fun saveAlarmMode(mode: Boolean) {
        dataStore.edit { prefs ->
            prefs[ALARM_MODE] = mode
        }
    }

    override suspend fun getAlarmMode(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }

            }
            .map { prefs ->
                prefs[ALARM_MODE] ?: true
            }
    }

    override suspend fun deleteAllData() {
        alarmInfoDao.deleteAllAlarmInfo()
    }

    override suspend fun insertAlarmInfo(alarmInfo: AlarmInfo) {
        alarmInfoDao.insertAlarmInfo(alarmInfo)
    }

    override suspend fun getAllAlarmInfo(): List<AlarmInfo> {
        return alarmInfoDao.getAll()
    }

    override suspend fun deleteAlarmInfo(alarmInfo: AlarmInfo) {
        alarmInfoDao.deleteAlarmInfo(alarmInfo)
    }

    override suspend fun deleteAlarmInfoUsingScheduleId(scheduleId: String) {
        alarmInfoDao.deleteAlarmInfoUsingScheduleId(scheduleId)
    }

    override suspend fun updateAlarmInfo(curTimeMillis: Long) {
        val alarmList = alarmInfoDao.getAll()
        val calendar = Calendar.getInstance()
        alarmList.forEach { alarmInfo ->
            calendar.timeInMillis = alarmInfo.endTime.toMilliseconds()
            calendar.add(Calendar.MINUTE, -alarmInfo.alarm.alarmTime - alarmInfo.estimatedTime)

            // 알림 시간 < 현재 시간 -> 알람 삭제 or 업데이트
            if (calendar.timeInMillis < curTimeMillis) {
                // 일회성 -> 알림 DB에서 삭제
                // 반복 -> DB 업데이트
                if (alarmInfo.repetition == null) {
                    alarmInfoDao.deleteAlarmInfo(alarmInfo)
                } else {
                    val oneDayMillis = 24 * 60 * 60 * 1000L
                    val interval = if (alarmInfo.repetition.cycleType == "DAILY") {
                        alarmInfo.repetition.cycleCount
                    } else {
                        alarmInfo.repetition.cycleCount * 7
                    }
                    while (calendar.timeInMillis < curTimeMillis) {
                        calendar.timeInMillis += (interval * oneDayMillis)
                    }
                    calendar.add(
                        Calendar.MINUTE,
                        alarmInfo.alarm.alarmTime + alarmInfo.estimatedTime
                    )
                    val endTime = DateTime(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
                    alarmInfoDao.updateAlarmInfo(
                        alarmInfo.copy(endTime = endTime)
                    )
                }
            }
        }
    }

    override suspend fun getDetailSchedule(scheduleId: String): ScheduleDetail {
        return api.getDetailSchedule(scheduleId).scheduleDetail
    }
}

