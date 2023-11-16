package com.boostcamp.planj.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemTodayScheduleBinding

class SegmentScheduleAdapterViewHolder(private val binding : ItemTodayScheduleBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : String, scheduleList: List<Schedule>){
        binding.tvMainScheduleTitle.text = item

        val scheduleAdapter = ScheduleAdapter()
        binding.rvMainSchedule.adapter = scheduleAdapter

        when(item){
            "일정" -> {
                scheduleAdapter.submitList(scheduleList.filter { !it.finished })
            }
            "완료" -> {
                scheduleAdapter.submitList(scheduleList.filter { it.finished && !it.failed })
            }
            "실패" -> {
                scheduleAdapter.submitList(scheduleList.filter { it.finished && it.failed })
            }
            else -> scheduleAdapter.submitList(scheduleList)
        }

    }
    companion object {
        fun from(parent: ViewGroup): SegmentScheduleAdapterViewHolder {
            return SegmentScheduleAdapterViewHolder(
                ItemTodayScheduleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}