package com.boostcamp.planj.ui.main.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.ItemListCategoryBinding

class CategoryListAdapterViewHolder(private val binding: ItemListCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        item: Category,
        clickListener: CategoryClickListener,
        categoryPopUpMenuListener: CategoryPopUpMenuListener
    ) {
        binding.category = item
        binding.executePendingBindings()

        itemView.setOnClickListener {
            clickListener.onClick(item)
        }

        binding.tvCategoryPopUpMenu.setOnClickListener {
            val popup = PopupMenu(binding.tvCategoryPopUpMenu.context, binding.tvCategoryPopUpMenu)
            popup.inflate(R.menu.category_popup_menu)
            popup.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.delete_category -> categoryPopUpMenuListener.delete(item)
                    R.id.edit_category -> categoryPopUpMenuListener.edit(item.categoryName)
                    else -> "오류"
                }
                true
            }
            popup.show()
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