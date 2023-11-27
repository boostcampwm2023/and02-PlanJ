package com.boostcamp.planj.ui.main.category

import com.boostcamp.planj.data.model.Category

interface CategoryPopUpMenuListener {

    fun edit(title: String)

    fun delete(category: Category)
}