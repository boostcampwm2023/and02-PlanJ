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
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.databinding.FragmentWeekBinding
import com.boostcamp.planj.ui.adapter.ScheduleClickListener
import com.boostcamp.planj.ui.adapter.ScheduleDoneListener
import com.boostcamp.planj.ui.adapter.SegmentScheduleAdapter
import com.boostcamp.planj.ui.adapter.SwipeListener
import com.boostcamp.planj.ui.main.home.week.adapter.CalendarAdapter
import com.boostcamp.planj.ui.main.home.week.adapter.CalendarVO
import com.boostcamp.planj.ui.main.home.week.adapter.ScheduleSimpleViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@AndroidEntryPoint
class WeekFragment : Fragment() {

    private var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeekFragmentViewModel by viewModels()
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var weekAdapter: SegmentScheduleAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setObserver()
        initAdapter()
        resultSchedule()
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

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scheduleList.collectLatest {
                    calendarAdapter.submitList(viewModel.makeWeekSchedule(viewModel.calendarList))
                    resultSchedule()
                    viewModel.scheduleList.collectLatest {
                        val list = resources.getStringArray(R.array.today_list)
                        it.sortedBy { schedule -> schedule.scheduleId }
                        val segment = listOf(
                            it.filter { s -> !s.isFinished },
                            it.filter { s -> s.isFinished && !s.isFailed },
                            it.filter { s -> s.isFinished && s.isFailed }
                        )
                        val sm = mutableListOf<ScheduleSegment>()
                        list.forEachIndexed { index, s ->
                            sm.add(ScheduleSegment(s, segment[index]))
                        }
                        weekAdapter.submitList(sm)
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter() {

        var calendarList = ArrayList<CalendarVO>()
        var week_day: Array<String> = resources.getStringArray(R.array.calendar_day)

        calendarList.run {
            val dateFormat = DateTimeFormatter.ofPattern("dd")

            var preSunday: LocalDateTime =
                (LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)))
            viewModel.today = LocalDateTime.now().format(dateFormat)
            if (LocalDateTime.now().dayOfWeek == DayOfWeek.SUNDAY) {
                preSunday = LocalDateTime.now()
            }

            for (i in 0..6) {

                calendarList.run {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat), week_day[i]))
                }
            }
        }

        viewModel.calendarList = calendarList
        calendarAdapter = CalendarAdapter()

        binding.rvWeekWeek.adapter = calendarAdapter
        binding.rvWeekWeek.layoutManager = GridLayoutManager(context, 7)
        calendarAdapter.submitList(viewModel.makeWeekSchedule(calendarList))

        val checkBoxListener = ScheduleDoneListener { _, _ ->
        }

        val clickListener = ScheduleClickListener {
        }
        val swipeListener = SwipeListener { _ ->
        }
        weekAdapter =
            SegmentScheduleAdapter(swipeListener, clickListener, checkBoxListener)
        binding.rvWeekScheduleList.adapter = weekAdapter
        weekAdapter.submitList(emptyList())
    }

    private fun resultSchedule() {

        val finishList = viewModel.scheduleList.value.filter { schedule: Schedule ->
            schedule.isFailed.not() && schedule.isFinished
        }
        setResultBtn(binding.btnWeekFinish, finishList, Color.BLUE)


        val failList =
            viewModel.scheduleList.value.filter { schedule: Schedule -> schedule.isFailed }
        setResultBtn(binding.btnWeekFail, failList, Color.RED)

        val haveList =
            viewModel.scheduleList.value.filter { schedule: Schedule -> schedule.isFinished.not() }
        setResultBtn(binding.btnWeekHave, haveList, Color.BLACK)


    }

    private fun setResultBtn(
        resultBtn: AppCompatButton,
        scheduleList: List<Schedule>,
        color: Int
    ) {
        val title = resultBtn.text.toString().split("\n")
        val countText = SpannableString(
            title[0].plus("\n${scheduleList.size}")
        )

        countText.setSpan(
            ForegroundColorSpan(color),
            title[0].length,
            countText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        resultBtn.text = countText
        resultBtn.setOnClickListener {
            makeDialog(scheduleList)
        }
    }

    private fun makeDialog(list: List<Schedule>) {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_schedule_result, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        val close = view.findViewById<ImageView>(R.id.iv_dialog_schedule_result_close)
        close.setOnClickListener {
            dialog.dismiss()
        }

        val scheduleView =
            view.findViewById<RecyclerView>(R.id.rv_dialog_schedule_result_week_schedule)
        scheduleView.adapter = ScheduleSimpleViewAdapter(list)


        dialog.show()
    }

}
