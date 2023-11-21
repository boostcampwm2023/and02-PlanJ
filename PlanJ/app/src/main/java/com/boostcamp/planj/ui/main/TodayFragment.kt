package com.boostcamp.planj.ui.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.FragmentTodayBinding
import com.boostcamp.planj.ui.main.adapter.SegmentScheduleAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var segmentScheduleAdapter: SegmentScheduleAdapter

    private val viewModel: TodayViewModel by viewModels()

    private lateinit var swipeListener : SwipeListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDate.now()
            "${current.monthValue}월 ${current.dayOfMonth}일"
        } else {
            val milli = System.currentTimeMillis()
            val t_dateFormat = SimpleDateFormat("MM-dd", Locale("ko", "KR"))
            val t_date = Date(milli)
            val str_date = t_dateFormat.format(t_date).split("-")
            "${str_date[0]}월 ${str_date[1]}일"
        }
        initBinding(today)
        swipeListener = SwipeListener {position ->
            val deleteSchedule = viewModel.deleteSchedule(position)
            Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
                setAction("Undo") {
                    viewModel.insertSchedule(deleteSchedule)
                }
            }.show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.schedules.collectLatest {
                    initAdapter(it)
                }
            }
        }
    }


    private fun initBinding(today: String) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.today = today
    }


    private fun initAdapter(scheduleList: List<Schedule>) {
        segmentScheduleAdapter = SegmentScheduleAdapter(scheduleList, swipeListener)
        binding.rvMainSchedule.adapter = segmentScheduleAdapter
        val list = resources.getStringArray(R.array.today_list).toList()
        segmentScheduleAdapter.submitList(list)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}