package com.boostcamp.planj.ui.main

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemWeekDayBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CalendarVO(
    var dayNumber: String = "", var dayOfWeek: String = ""
)

data class ScheduleType(
    val schedule: Schedule, val viewType: Int
)

class CalendarAdapter(
    private val calendarVOList: List<CalendarVO>,
    private val scheduleList: List<Schedule>,
    private val month: Int
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(private val binding: ItemWeekDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: CalendarVO, temp: List<Schedule>) {
            binding.tvDayNumber.text = item.dayNumber
            binding.tvDayWeek.text = item.dayOfWeek

            var today = binding.tvDayNumber.text


            val now = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko")))

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
                Log.d("DateToday", "${today}")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemWeekDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CalendarViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val temp = scheduleList.filter {
            it.startTime == null || it.startTime.split("[-:T]".toRegex())[1].toInt() == month || it.endTime.split(
                "[-:T]".toRegex()
            )[1].toInt() == month
        }
        holder.bind(calendarVOList[position], temp)
    }

    override fun getItemCount(): Int {
        return calendarVOList.size
    }

}