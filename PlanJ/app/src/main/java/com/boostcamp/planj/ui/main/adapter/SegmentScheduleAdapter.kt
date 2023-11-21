package com.boostcamp.planj.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.ui.main.SwipeListener

class SegmentScheduleAdapter(
    private val swipeListener: SwipeListener
) : ListAdapter<ScheduleSegment, SegmentScheduleAdapterViewHolder>(diffUtil) {


    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SegmentScheduleAdapterViewHolder {
        return SegmentScheduleAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SegmentScheduleAdapterViewHolder, position: Int) {
        holder.bind(currentList[position], swipeListener)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleSegment>() {
            override fun areContentsTheSame(
                oldItem: ScheduleSegment,
                newItem: ScheduleSegment
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: ScheduleSegment,
                newItem: ScheduleSegment
            ): Boolean {
                return oldItem === newItem
            }
        }
    }
}