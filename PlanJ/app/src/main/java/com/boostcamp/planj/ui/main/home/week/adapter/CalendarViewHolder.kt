package com.boostcamp.planj.ui.main.home.week.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.WeekSchedule
import com.boostcamp.planj.databinding.DialogScheduleResultBinding
import com.boostcamp.planj.databinding.ItemWeekDayBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class CalendarViewHolder(private val binding: ItemWeekDayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(scheduleList: WeekSchedule) {
        binding.tvWeekDayNumber.text = scheduleList.calendar.dayNumber
        binding.tvWeekDayWeek.text = scheduleList.calendar.dayOfWeek
        val today = scheduleList.calendar.dayNumber

        binding.layoutWeekDay.setOnClickListener {
            val layoutInflater = LayoutInflater.from(binding.root.context)
            val dialogBinding = DialogScheduleResultBinding.inflate(layoutInflater)

            val dialog = AlertDialog.Builder(binding.root.context)
                .setView(dialogBinding.root)
                .create()

            dialogBinding.ivDialogScheduleResultClose.setOnClickListener {
                dialog.dismiss()
            }

            val scheduleView = dialogBinding.rvDialogScheduleResultWeekSchedule

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
                "dd",
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