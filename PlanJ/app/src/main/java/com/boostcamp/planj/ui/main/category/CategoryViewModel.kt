package com.boostcamp.planj.ui.main.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.PostCategoryBody
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
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
            val currentTime = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale("kr", "ko")
            ).format(System.currentTimeMillis()).replace(" ", "T").trim()
            viewModelScope.launch(Dispatchers.IO) {
                val categoryBody = PostCategoryBody("01HFYAR1FX09FKQ2SW1HTG8BJ8", categoryName, currentTime)
                mainRepository.postCategory(categoryBody)
                    .catch {
                        Log.d("PLANJDEBUG", "postCategory Error ${it.message}")
                    }
                    .collect {
                        mainRepository.insertCategory(Category(it.categoryData.categoryUuid, categoryName))
                    }
            }
            CategoryState.SUCCESS
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteCategory(category)
        }
    }

    fun updateCategory(categoryName: String, title : String): CategoryState {
        return if (categoryName.isEmpty()) {
            CategoryState.EMPTY
        } else if (categories.value.map { c -> c.categoryName }
                .contains(categoryName)) {
            CategoryState.EXIST
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                categories.value.find { it.categoryName == title }?.let {category ->
                    mainRepository.updateCategory(category.copy(categoryName = categoryName))
                }
            }
            CategoryState.SUCCESS
        }
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        mainRepository.getUser().first()
    }
}