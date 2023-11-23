package com.boostcamp.planj.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.data.db.AppDatabase
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.DeleteCategoryBody
import com.boostcamp.planj.data.model.DeleteScheduleBody
import com.boostcamp.planj.data.model.PatchScheduleBody
import com.boostcamp.planj.data.model.PatchScheduleResponse
import com.boostcamp.planj.data.model.PostCategoryBody
import com.boostcamp.planj.data.model.PostCategoryResponse
import com.boostcamp.planj.data.model.PostScheduleBody
import com.boostcamp.planj.data.model.PostScheduleResponse
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.network.PlanJAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: PlanJAPI,
    private val db: AppDatabase,
    private val dataStore: DataStore<Preferences>
) : MainRepository {

    companion object {
        val USER = stringPreferencesKey("User")
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

    override fun postCategory(postCategoryBody: PostCategoryBody): Flow<PostCategoryResponse> =
        flow {
            emit(api.postCategory(postCategoryBody))
        }

    override fun postSchedule(
        userId: String,
        categoryId: String,
        title: String,
        endTime: String
    ): Flow<PostScheduleResponse> = flow {
        val postSchedule = PostScheduleBody(userId, categoryId, title, endTime)
        emit(api.postSchedule(postSchedule))
    }

    override fun getUser(): Flow<String> {
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

    override suspend fun deleteScheduleApi(userUuid: String, scheduleUuid: String) {
        api.deleteSchedule(DeleteScheduleBody(userUuid, scheduleUuid))
    }

    override suspend fun updateSchedule(schedule: Schedule) {
        db.scheduleDao().updateSchedule(schedule)
    }

    override fun patchSchedule(patchScheduleBody: PatchScheduleBody): Flow<PatchScheduleResponse> = flow {
        emit(api.patchSchedule(patchScheduleBody))
    }

    override suspend fun deleteCategoryApi(userUuid: String, scheduleUuid: String) {
        api.deleteCategory(DeleteCategoryBody(userUuid, scheduleUuid))
    }

    override suspend fun deleteScheduleUsingCategoryName(categoryName: String) {
        db.scheduleDao().deleteScheduleUsingCategory(categoryName)
    }


}