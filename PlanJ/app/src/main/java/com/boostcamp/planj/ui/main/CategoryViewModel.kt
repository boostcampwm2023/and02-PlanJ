package com.boostcamp.planj.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.repository.MainRepository
import com.boostcamp.planj.ui.category.CategoryState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val categories = mainRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun insertCategory(category: Category): CategoryState {
        return if (category.categoryName.isEmpty()) {
            CategoryState.EMPTY
        } else if (categories.value.map { c -> c.categoryName }
                .contains(category.categoryName)) {
            CategoryState.EXIST
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                mainRepository.insertCategory(category)
            }
            CategoryState.SUCCESS
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteCategory(category)
        }
    }

    fun updateCategory(category: Category): CategoryState {
        return if (category.categoryName.isEmpty()) {
            CategoryState.EMPTY
        } else if (categories.value.map { c -> c.categoryName }
                .contains(category.categoryName)) {
            CategoryState.EXIST
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                mainRepository.updateCategory(category)
            }
            CategoryState.SUCCESS
        }
    }
}