package com.boostcamp.planj.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.Category

class CategoryListAdapter : ListAdapter<Category, CategoryListAdapterViewHolder>(diffUtil) {


    override fun onBindViewHolder(holder: CategoryListAdapterViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListAdapterViewHolder {
        return CategoryListAdapterViewHolder.from(parent)
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Category>(){
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.categoryId == newItem.categoryId
            }

        }
    }
}