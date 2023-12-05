package com.boostcamp.planj.ui.main.category

import com.boostcamp.planj.data.model.Category

fun interface CategoryClickListener {
    fun onClick(category: Category)
}