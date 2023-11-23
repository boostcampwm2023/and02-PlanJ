package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.db.AppDatabase
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : MainRepository {

    override suspend fun insertSchedule(schedule: Schedule) {
        db.scheduleDao().insertSchedule(schedule)
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        db.scheduleDao().deleteSchedule(schedule)
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

    override suspend fun getCategoryTitleSchedule(title: String): List<Schedule> {
        return db.categoryDao().getCategoryTitleSchedule(title)
    }

    override fun getWeekSchedule(): Flow<List<Schedule>> {
        return db.scheduleDao().getWeekSchedule()
    }
}