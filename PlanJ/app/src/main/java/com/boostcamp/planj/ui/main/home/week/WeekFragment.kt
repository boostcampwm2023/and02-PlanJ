package com.boostcamp.planj.ui.main.home.week

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.FragmentWeekBinding
import com.boostcamp.planj.ui.main.home.week.adapter.CalendarAdapter
import com.boostcamp.planj.ui.main.home.week.adapter.CalendarVO
import com.boostcamp.planj.ui.main.home.week.adapter.ScheduleSimpleViewAdapter
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

        val scheduleList = listOf<Schedule>()

        initAdapter(scheduleList)
        resultSchedule(scheduleList)
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
            schedule.isFailed.not() && schedule.isFinished
        }
        val countFinish = SpannableString(
            binding.btnWeekFinish.text.toString().plus("${finishList.size}")
        )

        countFinish.setSpan(
            ForegroundColorSpan(Color.BLUE),
            binding.btnWeekFinish.text.length,
            countFinish.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.btnWeekFinish.text = countFinish
        binding.btnWeekFinish.setOnClickListener {
            makeDialog(finishList, true)
        }


        val failList = scheduleList.filter { schedule: Schedule -> schedule.isFailed }
        val countFail =
            SpannableString(binding.btnWeekFail.text.toString().plus("${failList.size}"))

        countFail.setSpan(
            ForegroundColorSpan(Color.RED),
            binding.btnWeekFail.text.length,
            countFail.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.btnWeekFail.text = countFail

        binding.btnWeekFail.setOnClickListener {
            makeDialog(failList, true)
        }


        val haveList = scheduleList.filter { schedule: Schedule -> schedule.isFinished.not() }
        val countHave =
            SpannableString(binding.btnWeekHave.text.toString().plus("${haveList.size}"))

        countHave.setSpan(
            ForegroundColorSpan(Color.BLACK),
            binding.btnWeekHave.text.length,
            countHave.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.btnWeekHave.text = countHave

        binding.btnWeekHave.setOnClickListener {
            makeDialog(haveList, true)
        }
    }

    fun makeDialog(list: List<Schedule>, complete: Boolean) {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_schedule_result, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        val close = view.findViewById<ImageView>(R.id.iv_dialog_close)
        close.setOnClickListener {
            dialog.dismiss()
        }

        val scheduleView = view.findViewById<RecyclerView>(R.id.rv_dialog_week_schedule)
        scheduleView.adapter = ScheduleSimpleViewAdapter(list)
        scheduleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        dialog.show()
    }

}
