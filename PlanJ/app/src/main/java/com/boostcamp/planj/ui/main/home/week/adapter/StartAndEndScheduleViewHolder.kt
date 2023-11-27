package com.boostcamp.planj.ui.main.home.week.adapter

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemWeekStartAndEndBinding

class StartAndEndScheduleViewHolder(binding: ItemWeekStartAndEndBinding, private val num: Int) :
    RecyclerView.ViewHolder(binding.root) {

    private val title = binding.tvWeekStartAndEndTitle
    private val bar = binding.vwWeekStartAndEndBar

    fun bind(item: Schedule) {
        title.text = item.title
        if (num == 2) {
            bar.setBackgroundColor(Color.RED)
        } else if (num == 0) {
            bar.setBackgroundColor(Color.GREEN)
        }
    }
}