package com.boostcamp.planj.ui.main.home


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.SaveDate


class CalendarAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<SaveDate, CalendarAdapterViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: CalendarAdapterViewHolder, position: Int) {
        holder.onBind(currentList[position], onClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapterViewHolder {
        return CalendarAdapterViewHolder.from(parent)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SaveDate>() {
            override fun areContentsTheSame(oldItem: SaveDate, newItem: SaveDate): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: SaveDate, newItem: SaveDate): Boolean {
                return oldItem.day == newItem.day
            }
        }
    }


}