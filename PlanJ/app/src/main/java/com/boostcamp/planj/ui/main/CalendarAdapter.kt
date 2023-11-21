package com.boostcamp.planj.ui.main

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemWeekDayBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.log

data class CalendarVO(
    var dayNumber: String = "", 
    var dayOfWeek: String = ""
)

class CalendarAdapter(
    private val calendarVOList: List<CalendarVO>,
    private val scheduleList: List<Schedule>
) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(private val binding: ItemWeekDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: CalendarVO) {
            binding.tvDayNumber.text = item.dayNumber
            binding.tvDayWeek.text = item.dayOfWeek

            var today = binding.tvDayNumber.text


            val now = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko")))
            if (today == now) {
                Log.d("DateToday","${today}")
                binding.tvDayWeek.setBackgroundColor(R.color.main1)
                binding.tvDayNumber.setBackgroundColor(R.color.main1)

                val weekScheduleAdapter = WeekScheduleAdapter(emptyList())
                binding.rvDaySchedule.adapter = weekScheduleAdapter
                binding.rvDaySchedule.layoutManager = LinearLayoutManager(binding.rvDaySchedule.context)

            } else {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            ItemWeekDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(calendarVOList[position])

    }

    override fun getItemCount(): Int {
        return calendarVOList.size
    }

}