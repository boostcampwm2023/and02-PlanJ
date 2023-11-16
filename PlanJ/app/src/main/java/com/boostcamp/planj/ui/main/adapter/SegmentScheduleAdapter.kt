package com.boostcamp.planj.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.ui.main.SwipeListener

class SegmentScheduleAdapter(private val scheduleList : List<Schedule>,
    private val swipeListener: SwipeListener) : ListAdapter<String, SegmentScheduleAdapterViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: SegmentScheduleAdapterViewHolder, position: Int) {
        holder.bind(currentList[position], scheduleList, swipeListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SegmentScheduleAdapterViewHolder {
        return SegmentScheduleAdapterViewHolder.from(parent)
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return  oldItem === newItem
            }
        }
    }
}