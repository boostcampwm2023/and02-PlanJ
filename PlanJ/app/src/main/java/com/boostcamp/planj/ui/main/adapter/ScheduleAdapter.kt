package com.boostcamp.planj.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.ui.main.ScheduleClickListener

class ScheduleAdapter(private val clickListener: ScheduleClickListener) : ListAdapter<Schedule, ScheduleSimpleViewHolder>(diffUtil) {

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ScheduleSimpleViewHolder, position: Int) {
        holder.bind(currentList[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleSimpleViewHolder {
        return ScheduleSimpleViewHolder.from(parent)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.scheduleId == newItem.scheduleId
            }
        }
    }
}