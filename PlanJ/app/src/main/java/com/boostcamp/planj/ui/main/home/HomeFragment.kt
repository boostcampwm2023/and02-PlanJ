package com.boostcamp.planj.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.databinding.FragmentHomeBinding
import com.boostcamp.planj.ui.UpdateWidget
import com.boostcamp.planj.ui.adapter.ScheduleClickListener
import com.boostcamp.planj.ui.adapter.ScheduleDoneListener
import com.boostcamp.planj.ui.adapter.SegmentScheduleAdapter
import com.boostcamp.planj.ui.adapter.SwipeListener
import com.boostcamp.planj.ui.schedule.ScheduleDialog
import com.boostcamp.planj.ui.schedule.ScheduleFailDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var segmentScheduleAdapter: SegmentScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        viewModel.listener = OnClickListener {
            viewModel.setDate(it)
        }
        viewModel.getCategories()
        initViewPager()
        initScheduleAdapter()
        setObserver()
        setListener()
    }

    private fun initScheduleAdapter() {
        val swipeListener = SwipeListener { schedule: Schedule ->
            viewModel.deleteSchedule(schedule)
            UpdateWidget.updateWidget(requireContext())

        }
        val scheduleClickListener = ScheduleClickListener { scheduleId ->
            val action =
                HomeFragmentDirections.actionFragmentHomeToScheduleActivity(scheduleId)
            findNavController().navigate(action)
        }
        val checkBoxListener = ScheduleDoneListener { schedule ->
            viewModel.scheduleFinishChange(schedule) {
                val dialog = ScheduleFailDialog(it) { schedule, memo ->
                    viewModel.postScheduleAddMemo(schedule, memo)
                }
                dialog.show(
                    parentFragmentManager, tag
                )
            }
            UpdateWidget.updateWidget(requireContext())
        }
        segmentScheduleAdapter = SegmentScheduleAdapter(
            swipeListener = swipeListener,
            checkBoxListener = checkBoxListener,
            clickListener = scheduleClickListener
        ) {
            viewModel.changeExpanded(it)
        }
        binding.rvMainHomeDailySchedule.adapter = segmentScheduleAdapter
        segmentScheduleAdapter.submitList(emptyList())
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectDate.collectLatest {
                    viewModel.setIsCurrent(binding.vpMainCalendarWeek.currentItem)
                    viewModel.getScheduleDaily("${it}T00:00:00")
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.schedules.collectLatest {
                    val list = listOf("일정", "완료", "실패")
                    val schedules = listOf(
                        it.filter { schedule -> !schedule.isFinished },
                        it.filter { schedule -> schedule.isFinished && !schedule.isFailed },
                        it.filter { schedule -> schedule.isFinished && schedule.isFailed }
                    )

                    val segmentList = mutableListOf<ScheduleSegment>()
                    repeat(3) { index ->
                        segmentList.add(ScheduleSegment(list[index], schedules[index]))
                    }
                    viewModel.setScheduleSegment(segmentList)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.failedSchedule.collectLatest {
                    it?.let { schedule ->
                        val dialog = ScheduleFailDialog(schedule) { s, memo ->
                            viewModel.postScheduleAddMemo(s, memo)
                        }
                        dialog.show(
                            childFragmentManager, tag
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scheduleSegment.collectLatest {
                    segmentScheduleAdapter.submitList(it)
                }
            }
        }
    }

    private fun initViewPager() {
        val calendarAdapter = CalendarFragmentStateAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )
        binding.vpMainCalendarWeek.offscreenPageLimit = 1
        binding.vpMainCalendarWeek.adapter = calendarAdapter
        binding.vpMainCalendarWeek.setCurrentItem(Int.MAX_VALUE / 2, false)

        binding.vpMainCalendarWeek.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.WEEK_OF_MONTH, position - (Int.MAX_VALUE / 2))
                calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DAY_OF_WEEK))
                val date = SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(calendar.time)
                viewModel.setCalendarTitle(date)
                viewModel.setIsCurrent(position)
            }
        })
    }


    private fun setListener() {
        binding.fbAddSchedule.setOnClickListener {
            val dialog = ScheduleDialog(
                viewModel.categories.value.map { it.categoryName },
                "미분류",
                true
            ) { category, title, _ ->
                viewModel.postSchedule(category, title)
                UpdateWidget.updateWidget(requireContext())
            }
            dialog.show(
                childFragmentManager, null
            )


        }

        binding.btnMainCurrentDate.setOnClickListener {
            binding.vpMainCalendarWeek.setCurrentItem(Int.MAX_VALUE / 2, false)
            val now = LocalDate.now()
            Log.d("ABCDE", "${now.year}-${now.monthValue + 1}-${now.dayOfMonth}")
            viewModel.setDate(
                "${now.year}-${String.format("%02d", now.monthValue)}-${
                    String.format(
                        "%02d",
                        now.dayOfMonth
                    )
                }"
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}