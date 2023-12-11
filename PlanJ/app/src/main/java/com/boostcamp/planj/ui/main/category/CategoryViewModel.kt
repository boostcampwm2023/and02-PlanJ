package com.boostcamp.planj.ui.main.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.dto.PostCategoryBody
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()


    fun postCategory(categoryName: String): CategoryState {
        return if (categoryName.isEmpty()) {
            CategoryState.EMPTY
        } else if (_categories.value.map { c -> c.categoryName }
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
                        //TODO 카데고리 조회 요청 api 쏘기
                        getCategory()
                    }
            }
            CategoryState.SUCCESS
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {

            mainRepository.deleteCategoryApi(category.categoryUuid)
                .catch {
                    Log.d("PLANJDEBUG", "category delete error  ${it.message}")
                }
                .collectLatest {
                    //TODO 카테고리 조회 요청 api 쏘기
                    getCategory()
                }

        }
    }

    fun patchCategory(categoryName: String, title: String): CategoryState {
        return if (categoryName.isEmpty()) {
            CategoryState.EMPTY
        } else if (_categories.value.map { c -> c.categoryName }
                .contains(categoryName)) {
            CategoryState.EXIST
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _categories.value.find { it.categoryName == title }?.let { category ->
                    val categoryId = category.categoryUuid
                    mainRepository.updateCategoryApi(categoryId, title)
                        .catch {
                            Log.d("PLANJBUG", "updateCategoryApi error ${it.message}")
                        }
                        .collectLatest {
                            //TODO 카테고리 조회 요청 api 쓰기
                            getCategory()
                        }
                }
            }
            CategoryState.SUCCESS
        }
    }

    fun getCategory() {
        viewModelScope.launch {
            mainRepository.getCategoryListApi()
                .catch {
                    Log.d("PLANJDEBUG", "getCategory error ${it.message}")
                }
                .collectLatest {
                    _categories.value = it
                }
        }
    }
}