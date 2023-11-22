package com.boostcamp.planj.ui.main.home.week.adapter

import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemWeekDayBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class CalendarViewHolder(private val binding: ItemWeekDayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CalendarVO, temp: List<Schedule>) {
        binding.tvDayNumber.text = item.dayNumber
        binding.tvDayWeek.text = item.dayOfWeek

        var today = binding.tvDayNumber.text

        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
                .format(
                    DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko"))
                )
        } else {
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
        }

        val haveStart = temp.filter {
            it.startTime != null && it.startTime.split("[-:T]".toRegex())[2].toInt() == today.toString()
                .toInt()
        }

        val emptyStart =
            temp.filter { it.startTime == null && it.endTime.split("[-:T]".toRegex())[2].toInt() > now.toInt() }

        val endList = temp.filter {
            it.endTime.split("[-:T]".toRegex())[2].toInt() == today.toString().toInt()
        }


        val totalList: MutableList<ScheduleType> = mutableListOf()

        haveStart.forEach { schedule: Schedule ->
            totalList.add(ScheduleType(schedule, 0))
        }

        endList.forEach { schedule: Schedule ->
            totalList.add(ScheduleType(schedule, 2))
        }



        if (today == now) {
            binding.tvDayWeek.setBackgroundColor(R.color.main1)
            binding.tvDayNumber.setBackgroundColor(R.color.main1)

            emptyStart.forEach { schedule: Schedule ->
                totalList.add(ScheduleType(schedule, 1))
            }



            addAdapter(WeekScheduleAdapter(totalList))

        } else if (today.toString().toInt() < now.toInt()) {
            addAdapter(WeekScheduleAdapter(totalList))

        } else if (today.toString().toInt() > now.toInt()) {
            addAdapter(WeekScheduleAdapter(totalList))

        }

    }

    private fun addAdapter(weekScheduleAdapter: WeekScheduleAdapter) {
        binding.rvDaySchedule.adapter = weekScheduleAdapter
        binding.rvDaySchedule.layoutManager =
            LinearLayoutManager(binding.rvDaySchedule.context)
    }
}