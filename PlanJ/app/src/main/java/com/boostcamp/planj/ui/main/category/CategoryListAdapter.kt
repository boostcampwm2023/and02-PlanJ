package com.boostcamp.planj.ui.main.category

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.Category

class CategoryListAdapter(
    private val clickListener: CategoryClickListener,
    private val popUpClickListener: CategoryPopUpMenuListener
) : ListAdapter<Category, CategoryListAdapterViewHolder>(diffUtil) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListAdapterViewHolder {
        return CategoryListAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryListAdapterViewHolder, position: Int) {
        holder.onBind(currentList[position], clickListener, popUpClickListener)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Category>() {
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.categoryUuid == newItem.categoryUuid
            }

        }
    }
}