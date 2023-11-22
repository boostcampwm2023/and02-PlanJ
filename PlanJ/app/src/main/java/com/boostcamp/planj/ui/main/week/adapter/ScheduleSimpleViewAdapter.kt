package com.boostcamp.planj.ui.main.week.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule


class ScheduleSimpleViewAdapter(private val scheduleList: List<Schedule>) :
    RecyclerView.Adapter<ScheduleSimpleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleSimpleViewHolder {
        return ScheduleSimpleViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ScheduleSimpleViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

}