package com.boostcamp.planj.ui.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boostcamp.planj.databinding.FragmentTodayBinding
import com.boostcamp.planj.ui.main.adapter.ScheduleAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private lateinit var todaySchedule : ScheduleAdapter
    private lateinit var completeSchedule : ScheduleAdapter
    private lateinit var failSchedule : ScheduleAdapter

    //더미 일정
    private val dummyList = DummySchedule.getDummyList()

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
        binding.lifecycleOwner = viewLifecycleOwner
        binding.today = today
        initAdapter()


    }

    private fun initAdapter() {
        todaySchedule = ScheduleAdapter()
        completeSchedule = ScheduleAdapter()
        failSchedule = ScheduleAdapter()

        binding.rvMainTodayschedule.adapter = todaySchedule
        binding.rvMainCompleteschedule.adapter = completeSchedule
        binding.rvMainFailschedule.adapter = failSchedule

        todaySchedule.submitList(dummyList.filter { !it.finished })
        completeSchedule.submitList(dummyList.filter { it.finished && !it.failed })
        failSchedule.submitList(dummyList.filter { it.finished && it.failed })
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