package com.boostcamp.planj.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentWeekBinding
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class WeekFragment : Fragment() {
    private var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter() {
        lateinit var calendarAdapter: CalendarAdapter
        var calendarList = ArrayList<CalendarVO>()
        val scheduleList = DummySchedule.getDummyList()


        var week_day: Array<String> = resources.getStringArray(R.array.calendar_day)

        var month: Int


        calendarList.run {
            val dateFormat =
                DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko"))
            val monthFormat = DateTimeFormatter.ofPattern("yyyy-MM-").withLocale(Locale.forLanguageTag("ko"))

            month = LocalDateTime.now().format(monthFormat).split("-")[1].toInt()

            var preSunday: LocalDateTime =
                LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
            if(LocalDateTime.now().dayOfWeek==DayOfWeek.SUNDAY){
                preSunday= LocalDateTime.now()
            }

            for (i in 0..6) {

                calendarList.run {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat), week_day[i]))
                }
            }
        }

        calendarAdapter = CalendarAdapter(calendarList, scheduleList, month)
        binding.rvWeekWeek.adapter = calendarAdapter
        binding.rvWeekWeek.layoutManager = GridLayoutManager(context, 7)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}