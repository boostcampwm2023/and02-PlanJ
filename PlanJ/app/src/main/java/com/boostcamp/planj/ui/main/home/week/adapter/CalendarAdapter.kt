package com.boostcamp.planj.ui.main.home.week.adapter


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.WeekSchedule

data class CalendarVO(
    var dayNumber: String = "", var dayOfWeek: String = ""
)

data class ScheduleType(
    val schedule: Schedule, val viewType: Int
)

class CalendarAdapter() :
    ListAdapter<WeekSchedule, CalendarViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<WeekSchedule>() {
            override fun areItemsTheSame(oldItem: WeekSchedule, newItem: WeekSchedule): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: WeekSchedule, newItem: WeekSchedule): Boolean {
                return oldItem == newItem
            }
        }
    }
}