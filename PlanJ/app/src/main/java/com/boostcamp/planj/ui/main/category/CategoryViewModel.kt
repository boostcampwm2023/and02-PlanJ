package com.boostcamp.planj.ui.main.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.dto.PostCategoryBody
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val categories = mainRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun insertCategory(categoryName: String): CategoryState {
        return if (categoryName.isEmpty()) {
            CategoryState.EMPTY
        } else if (categories.value.map { c -> c.categoryName }
                .contains(categoryName)) {
            CategoryState.EXIST
        } else {

            viewModelScope.launch(Dispatchers.IO) {
                val categoryBody = PostCategoryBody(categoryName)
                mainRepository.postCategory(categoryBody)
                    .catch {
                        Log.d("PLANJDEBUG", "postCategory Error ${it.message}")
                    }
                    .collect {
                        mainRepository.insertCategory(
                            Category(
                                it.categoryData.categoryUuid,
                                categoryName
                            )
                        )
                    }
            }
            CategoryState.SUCCESS
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainRepository.deleteCategoryApi(category.categoryId)
                mainRepository.deleteCategory(category)
                mainRepository.deleteScheduleUsingCategoryName(category.categoryName)
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "category delete error  ${e.message}")
            }
        }
    }

    fun updateCategory(categoryName: String, title: String): CategoryState {
        return if (categoryName.isEmpty()) {
            CategoryState.EMPTY
        } else if (categories.value.map { c -> c.categoryName }
                .contains(categoryName)) {
            CategoryState.EXIST
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                categories.value.find { it.categoryName == title }?.let { category ->
                    val categoryId = category.categoryId
                    mainRepository.updateCategory(category.copy(categoryName = categoryName))
                    mainRepository.updateScheduleUsingCategory(title, categoryName)
                    mainRepository.updateCategoryApi(categoryId, title)
                }
            }
            CategoryState.SUCCESS
        }
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        mainRepository.getUser().first()
    }
}