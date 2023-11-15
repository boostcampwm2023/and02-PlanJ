package com.boostcamp.planj.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.Schedule
import com.boostcamp.planj.databinding.ItemWeekScheduleBinding

class WeekScheduleAdapter(private val scheduleList: List<Schedule>) :
    RecyclerView.Adapter<WeekScheduleAdapter.WeekScheduleViewHolder>() {

    class WeekScheduleViewHolder(private val binding: ItemWeekScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Schedule) {
            binding.vwScheduleName.text = item.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekScheduleViewHolder {
        val binding =
            ItemWeekScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekScheduleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: WeekScheduleViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

}