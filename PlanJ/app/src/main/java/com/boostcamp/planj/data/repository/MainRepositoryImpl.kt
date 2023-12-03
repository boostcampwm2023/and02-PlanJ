package com.boostcamp.planj.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.data.db.AlarmInfoDao
import com.boostcamp.planj.data.db.CategoryDao
import com.boostcamp.planj.data.db.ScheduleDao
import com.boostcamp.planj.data.db.UserDao
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.DeleteScheduleBody
import com.boostcamp.planj.data.model.dto.GetCategoryResponse
import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import com.boostcamp.planj.data.model.dto.PatchCategoryRequest
import com.boostcamp.planj.data.model.dto.PatchCategoryResponse
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
    private val userDao: UserDao,
    private val scheduleDao: ScheduleDao,
    private val alarmInfoDao: AlarmInfoDao,
    private val categoryDao: CategoryDao
) : MainRepository {

    companion object {
        val USER = stringPreferencesKey("User")
        val ALARM_MODE = booleanPreferencesKey("alarm")
    }

    override suspend fun insertSchedule(schedule: Schedule) {
        scheduleDao.insertSchedule(schedule)
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        scheduleDao.deleteSchedule(schedule)
    }

    override suspend fun deleteScheduleUsingId(id: String) {
        scheduleDao.deleteScheduleUsingId(id)
    }

    override fun getSchedules(): Flow<List<Schedule>> {
        return scheduleDao.getSchedules()
    }

    override fun getCategories(): Flow<List<String>> {
        return categoryDao.getCategories()
    }

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategory()
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    override fun getCategoryTitleSchedule(title: String): Flow<List<Schedule>> {
        return if (title == "전체 일정") {
            scheduleDao.getSchedules()
        } else {
            scheduleDao.getCategoryTitleSchedule(title)
        }
    }

    override suspend fun insertUser(email: String) {
        userDao.insertUser(User("aaa", email, email))
    }

    override suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }

    override fun getAllUser(): Flow<List<User>> {
        return userDao.getAllUser()
    }

    override fun searchSchedule(input: String): Flow<List<Schedule>> {
        return scheduleDao.searchSchedule(input)
    }

    override fun postCategory(postCategoryBody: PostCategoryBody): Flow<PostCategoryResponse> =
        flow {
            emit(api.postCategory(postCategoryBody))
        }

    override fun postSchedule(
        categoryId: String,
        title: String,
        endTime: String
    ): Flow<PostScheduleResponse> = flow {
        val postSchedule = PostScheduleBody(categoryId, title, endTime)
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

    override fun getCategory(categoryName: String): Category {
        return categoryDao.getCategory(categoryName)
    }

    override suspend fun deleteScheduleApi(scheduleUuid: String) {
        api.deleteSchedule(DeleteScheduleBody(scheduleUuid))
    }

    override suspend fun updateSchedule(schedule: Schedule) {
        scheduleDao.updateSchedule(schedule)
    }

    override fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse> =
        flow {
            emit(api.patchSchedule(patchScheduleBody))
        }

    override suspend fun deleteCategoryApi(categoryUuid: String) {
        api.deleteCategory(categoryUuid)
    }

    override suspend fun deleteScheduleUsingCategoryName(categoryName: String) {
        scheduleDao.deleteScheduleUsingCategory(categoryName)
    }

    override suspend fun updateCategoryApi(
        categoryUuid: String,
        categoryName: String
    ): Flow<PatchCategoryResponse> = flow {
        emit(api.patchCategory(PatchCategoryRequest(categoryUuid, categoryName)))
    }

    override suspend fun updateScheduleUsingCategory(
        categoryNameBefore: String,
        categoryAfter: String
    ) {
        scheduleDao.updateScheduleUsingCategory(categoryNameBefore, categoryAfter)
    }

    override fun getWeekSchedule(): Flow<List<Schedule>> {
        return scheduleDao.getWeekSchedule()
    }

    override suspend fun getCategoryListApi(): Flow<GetCategoryResponse> = flow {
        emit(api.getCategoryList())
    }

    override suspend fun getCategorySchedulesApi(categoryUuid: String): Flow<GetSchedulesResponse> =
        flow {
            emit(api.getCategorySchedule(categoryUuid))
        }

    override suspend fun getWeeklyScheduleApi(date: String): Flow<GetSchedulesResponse> = flow {
        emit(api.getWeeklySchedule(date))
    }

    override suspend fun getDailyScheduleApi(date: String): Flow<GetSchedulesResponse> = flow {
        emit(api.getDailySchedule(date))
    }

    override suspend fun postFriendApi(friendEmail: String) {
        api.postFriend(PostFriendRequest(friendEmail))
    }

    override suspend fun getFriendsApi(): Flow<List<User>> = flow {
        val friendInfo = api.getFriends().data
        val user = mutableListOf<User>()
        friendInfo.forEach { friendInfo ->
            user.add(User(friendInfo.profileUrl, friendInfo.nickname, friendInfo.email))
        }
        emit(user.toList())
    }

    override suspend fun deleteAccount() {
        api.deleteAccount()
    }

    override suspend fun getMyInfo(): Flow<User> = flow {
        val myInfo = api.getMyInfo().data

        emit(User(imgUrl = myInfo.imgUrl, email = myInfo.email, nickname = myInfo.nickname))
    }

    override fun postUser(
        nickName: String,
        imageFile: MultipartBody.Part?
    ): Flow<PostUserResponse> = flow {
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
                prefs[ALARM_MODE] ?: false
            }
    }

    override suspend fun deleteAllData() {
        scheduleDao.deleteAllSchedule()
        categoryDao.deleteAllCategory()
        userDao.deleteAllUser()
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