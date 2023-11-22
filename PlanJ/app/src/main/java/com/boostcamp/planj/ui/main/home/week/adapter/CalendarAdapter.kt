package com.boostcamp.planj.ui.main.home.week.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemWeekDayBinding

data class CalendarVO(
    var dayNumber: String = "", var dayOfWeek: String = ""
)

data class ScheduleType(
    val schedule: Schedule, val viewType: Int
)

class CalendarAdapter(
    private val calendarVOList: List<CalendarVO>,
    private val scheduleList: List<Schedule>
) : RecyclerView.Adapter<CalendarViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemWeekDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CalendarViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(calendarVOList[position], scheduleList)
    }

    override fun getItemCount(): Int {
        return calendarVOList.size
    }

}