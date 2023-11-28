package com.boostcamp.planj.ui.main.home.week.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.WeekSchedule
import com.boostcamp.planj.databinding.ItemWeekDayBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class CalendarViewHolder(private val binding: ItemWeekDayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(scheduleList: WeekSchedule, context: Context) {
        binding.tvWeekDayNumber.text = scheduleList.calendar.dayNumber
        binding.tvWeekDayWeek.text = scheduleList.calendar.dayOfWeek
        val today = scheduleList.calendar.dayNumber

        binding.layoutWeekDay.setOnClickListener {

            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.dialog_schedule_result, null)

            val dialog = AlertDialog.Builder(context)
                .setView(view)
                .create()

            val close = view.findViewById<ImageView>(R.id.iv_dialog_schedule_result_close)
            close.setOnClickListener {
                dialog.dismiss()
            }

            val scheduleView =
                view.findViewById<RecyclerView>(R.id.rv_dialog_schedule_result_week_schedule)

            scheduleView.adapter = ScheduleSimpleViewAdapter(changeList(scheduleList))


            dialog.show()

        }

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


        if (today == now) {
            binding.tvWeekDayWeek.setBackgroundColor(R.color.main1)
            binding.tvWeekDayNumber.setBackgroundColor(R.color.main1)
        }

        addAdapter(WeekScheduleAdapter(scheduleList.scheduleList))

    }

    private fun addAdapter(weekScheduleAdapter: WeekScheduleAdapter) {
        binding.rvWeekDaySchedule.adapter = weekScheduleAdapter
        binding.rvWeekDaySchedule.layoutManager =
            LinearLayoutManager(binding.rvWeekDaySchedule.context)
    }

    companion object {
        fun from(parent: ViewGroup): CalendarViewHolder {
            return CalendarViewHolder(
                ItemWeekDayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    private fun changeList(weekScheduleList: WeekSchedule): List<Schedule> {
        val resultList: MutableList<Schedule> = mutableListOf()
        weekScheduleList.scheduleList.forEach { scheduleType: ScheduleType ->
            resultList.add(scheduleType.schedule)
        }
        return resultList
    }
}