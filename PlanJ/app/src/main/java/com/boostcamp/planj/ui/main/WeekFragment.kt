package com.boostcamp.planj.ui.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.FragmentWeekBinding
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class WeekFragment : Fragment() {
    private var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleList = DummySchedule.getDummyList()

        initAdapter(scheduleList)
        resultSchedule(scheduleList)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter(scheduleList: List<Schedule>) {

        lateinit var calendarAdapter: CalendarAdapter
        var calendarList = ArrayList<CalendarVO>()
        var week_day: Array<String> = resources.getStringArray(R.array.calendar_day)


        calendarList.run {
            val dateFormat = DateTimeFormatter.ofPattern("dd")
            val monthFormat = DateTimeFormatter.ofPattern("yyyy-MM-")

            var preSunday: LocalDateTime =
                (LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)))
            if (LocalDateTime.now().dayOfWeek == DayOfWeek.SUNDAY) {
                preSunday = LocalDateTime.now()
            }

            for (i in 0..6) {

                calendarList.run {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat), week_day[i]))
                }
            }
        }

        calendarAdapter = CalendarAdapter(calendarList, scheduleList)
        binding.rvWeekWeek.adapter = calendarAdapter
        binding.rvWeekWeek.layoutManager = GridLayoutManager(context, 7)
    }

    private fun resultSchedule(scheduleList: List<Schedule>) {

        val finishList = scheduleList.filter { schedule: Schedule ->
            schedule.failed.not() && schedule.finished
        }
        val countFinish = SpannableString(
            binding.btnWeekFinish.text.toString().plus("${finishList.size}")
        )

        countFinish.setSpan(
            ForegroundColorSpan(Color.GREEN),
            binding.btnWeekFinish.text.length,
            countFinish.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.btnWeekFinish.text = countFinish


        val failList = scheduleList.filter { schedule: Schedule -> schedule.failed }
        val countFail =
            SpannableString(binding.btnWeekFail.text.toString().plus("${failList.size}"))

        countFail.setSpan(
            ForegroundColorSpan(Color.RED),
            binding.btnWeekFail.text.length,
            countFail.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.btnWeekFail.text = countFail


        val haveList = scheduleList.filter { schedule: Schedule -> schedule.finished.not() }
        val countHave =
            SpannableString(binding.btnWeekHave.text.toString().plus("${haveList.size}"))

        countHave.setSpan(
            ForegroundColorSpan(Color.BLUE),
            binding.btnWeekHave.text.length,
            countHave.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.btnWeekHave.text = countHave
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
