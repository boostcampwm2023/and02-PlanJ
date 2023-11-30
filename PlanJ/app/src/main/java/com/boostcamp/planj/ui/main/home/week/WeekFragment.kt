package com.boostcamp.planj.ui.main.home.week

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
import com.boostcamp.planj.databinding.DialogScheduleResultBinding
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
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            var preSunday: LocalDateTime =
                (LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)))
            viewModel.today = LocalDateTime.now().format(dateFormat)
            if (LocalDateTime.now().dayOfWeek == DayOfWeek.SUNDAY) {
                preSunday = LocalDateTime.now()
            }

            for (i in 0..6) {

                calendarList.run {
                    add(
                        CalendarVO(
                            preSunday.plusDays(i.toLong())
                                .format(DateTimeFormatter.ofPattern("dd")), week_day[i]
                        )
                    )
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
        binding.rvWeekScheduleList.isNestedScrollingEnabled = false
        weekAdapter.submitList(emptyList())
    }

    private fun resultSchedule() {

        val finishList = mutableListOf<Schedule>()
        val failList = mutableListOf<Schedule>()
        val haveList = mutableListOf<Schedule>()

        viewModel.scheduleList.value.forEach { schedule: Schedule ->
            if (schedule.isFinished) {
                finishList.add(schedule)
            } else {
                haveList.add(schedule)
            }
            if (schedule.isFailed) {
                failList.add(schedule)
            }
        }
        binding.tvWeekFinishCount.text = finishList.size.toString()
        binding.tvWeekFailCount.text = failList.size.toString()
        binding.tvWeekHaveCount.text = haveList.size.toString()

        binding.btnWeekFinish.setOnClickListener {
            makeDialog(finishList, getString(R.string.completeSchedule))
        }
        binding.btnWeekFail.setOnClickListener {
            makeDialog(failList, getString(R.string.failSchedule))
        }
        binding.btnWeekHave.setOnClickListener {
            makeDialog(haveList, getString(R.string.haveSchedule))
        }
    }


    private fun makeDialog(list: List<Schedule>, title: String) {
    val layoutInflater = LayoutInflater.from(requireContext())
    val dialogBinding = DialogScheduleResultBinding.inflate(layoutInflater)
    dialogBinding.tvDialogScheduleResultText.text = title

    val dialog = AlertDialog.Builder(requireContext())
        .setView(dialogBinding.root)
        .create()

    dialogBinding.ivDialogScheduleResultClose.setOnClickListener{
        dialog.dismiss()
    }
    dialogBinding.rvDialogScheduleResultWeekSchedule.adapter = ScheduleSimpleViewAdapter(list)


    dialog.show()
    }

}
