package com.boostcamp.planj.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.ItemListCategoryBinding
import com.boostcamp.planj.databinding.ItemScheduleBinding
import com.boostcamp.planj.ui.main.CategoryClickListener

class CategoryListAdapterViewHolder(private val binding: ItemListCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: Category, clickListener: CategoryClickListener) {
        binding.category = item
        binding.executePendingBindings()

        itemView.setOnClickListener {
            clickListener.onClick()
        }
    }

    companion object {
        fun from(parent: ViewGroup): CategoryListAdapterViewHolder {
            return CategoryListAdapterViewHolder(
                ItemListCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}