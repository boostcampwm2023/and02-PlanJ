package com.boostcamp.planj.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.data.db.AppDatabase
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.ScheduleDummy
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
    private val db: AppDatabase,
    private val dataStore: DataStore<Preferences>
) : MainRepository {

    companion object {
        val USER = stringPreferencesKey("User")
        val ALARM_MODE = booleanPreferencesKey("alarm")
    }

    override suspend fun insertSchedule(schedule: Schedule) {
        db.scheduleDao().insertSchedule(schedule)
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        db.scheduleDao().deleteSchedule(schedule)
    }

    override suspend fun deleteScheduleUsingId(id: String) {
        db.scheduleDao().deleteScheduleUsingId(id)
    }

    override fun getSchedules(): Flow<List<Schedule>> {
        return db.scheduleDao().getSchedules()
    }

    override fun getCategories(): Flow<List<String>> {
        return db.categoryDao().getCategories()
    }

    override suspend fun insertCategory(category: Category) {
        db.categoryDao().insertCategory(category)
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return db.categoryDao().getAllCategory()
    }

    override suspend fun deleteCategory(category: Category) {
        db.categoryDao().deleteCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        db.categoryDao().updateCategory(category)
    }

    override fun getCategoryTitleSchedule(title: String): Flow<List<Schedule>> {
        return if (title == "전체 일정") {
            db.scheduleDao().getSchedules()
        } else {
            db.scheduleDao().getCategoryTitleSchedule(title)
        }
    }

    override suspend fun insertUser(email: String) {
        db.userDao().insertUser(User("aaa", email, email))
    }

    override suspend fun deleteUser(email: String) {
        db.userDao().deleteUser(email)
    }

    override fun getAllUser(): Flow<List<User>> {
        return db.userDao().getAllUser()
    }

    override fun searchSchedule(input: String): Flow<List<Schedule>> {
        return db.scheduleDao().searchSchedule(input)
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
        return db.categoryDao().getCategory(categoryName)
    }

    override suspend fun deleteScheduleApi(scheduleUuid: String) {
        api.deleteSchedule(DeleteScheduleBody(scheduleUuid))
    }

    override suspend fun updateSchedule(schedule: Schedule) {
        db.scheduleDao().updateSchedule(schedule)
    }

    override fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse> =
        flow {
            emit(api.patchSchedule(patchScheduleBody))
        }

    override suspend fun deleteCategoryApi(categoryUuid: String) {
        api.deleteCategory(categoryUuid)
    }

    override suspend fun deleteScheduleUsingCategoryName(categoryName: String) {
        db.scheduleDao().deleteScheduleUsingCategory(categoryName)
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
        db.scheduleDao().updateScheduleUsingCategory(categoryNameBefore, categoryAfter)
    }

    override fun getWeekSchedule(): Flow<List<Schedule>> {
        return db.scheduleDao().getWeekSchedule()
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

    override suspend fun getDailyScheduleApi(date: String): Flow<List<ScheduleDummy>> = flow {
        try {
            val scheduleInfo = api.getDailySchedule(date)
            val scheduleDummy = scheduleInfo.date.map {

                val startAt = it.startAt?.split("T","-",":")?.map { time -> time.toInt() } ?: emptyList()
                val endAt = it.endAt.split("T","-",":").map { time -> time.toInt() }

                ScheduleDummy(
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
        val friendInfo = api.getFriends().data
        val user = mutableListOf<User>()
        friendInfo.forEach { friendInfo ->
            user.add(User("", friendInfo.nickname, friendInfo.email))
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

    override fun postUser(nickName : String, imageFile : MultipartBody.Part?): Flow<PostUserResponse> = flow {
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
        db.scheduleDao().deleteAllSchedule()
        db.categoryDao().deleteAllCategory()
        db.userDao().deleteAllUser()
        db.alarmInfoDao().deleteAllAlarmInfo()
    }
}