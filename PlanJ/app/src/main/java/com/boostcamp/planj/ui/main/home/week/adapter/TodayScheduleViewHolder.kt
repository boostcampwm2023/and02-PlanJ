package com.boostcamp.planj.ui.main.home.week.adapter

import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemWeekScheduleBinding


class TodayScheduleViewHolder(binding: ItemWeekScheduleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val title = binding.vwScheduleTitle

    fun bind(item: Schedule) {
        title.text = item.title
    }
}