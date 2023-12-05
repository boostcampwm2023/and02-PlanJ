package com.boostcamp.planj.ui.categorydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.databinding.FragmentCategoryDetailBinding
import com.boostcamp.planj.ui.adapter.ScheduleClickListener
import com.boostcamp.planj.ui.adapter.ScheduleDoneListener
import com.boostcamp.planj.ui.adapter.SegmentScheduleAdapter
import com.boostcamp.planj.ui.adapter.SwipeListener
import com.boostcamp.planj.ui.schedule.ScheduleDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailFragment : Fragment() {
    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryDetailViewModel by activityViewModels()
    private lateinit var segmentScheduleAdapter: SegmentScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.lifecycleOwner=viewLifecycleOwner
        viewModel.getCategoryDetailSchedules()
        initAdapter()
        setObserver()
        setListener()
    }

    private fun setListener() {
        binding.fbCategoryDetailAddSchedule.setOnClickListener {
            val dialog = ScheduleDialog(
                emptyList(),
                initText = viewModel.category.value.categoryName,
                false
            ) { category, title, endTime ->
                viewModel.postSchedule(category, title, endTime)
            }
            activity?.supportFragmentManager?.let {
                dialog.show(it, null)
            }
        }
        binding.ivCategoryDetailBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initAdapter() {
        val swipeListener = SwipeListener {
            viewModel.deleteSchedule(it)
        }
        val scheduleClickListener = ScheduleClickListener { scheduleId ->
            val action =
                CategoryDetailFragmentDirections.actionCategoryDetailFragmentToScheduleActivity(
                    scheduleId
                )
            findNavController().navigate(action)
        }
        val checkBoxListener = ScheduleDoneListener { schedule ->
            viewModel.checkBoxChange(schedule, !schedule.isFinished)
        }
        segmentScheduleAdapter =
            SegmentScheduleAdapter(swipeListener, scheduleClickListener, checkBoxListener)
        binding.rvCategoryDetail.adapter = segmentScheduleAdapter
        segmentScheduleAdapter.submitList(emptyList())
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.schedules.collectLatest {
                    it.sortedBy { schedule -> schedule.scheduleId }
                    val segment = listOf(
                        it.filter { s -> !s.isFinished },
                        it.filter { s -> s.isFinished && !s.isFailed },
                        it.filter { s -> s.isFinished && s.isFailed }
                    )
                    val list = resources.getStringArray(R.array.today_list)
                    val segmentList = mutableListOf<ScheduleSegment>()
                    (0..2).forEach { index ->
                        segmentList.add(ScheduleSegment(list[index], segment[index]))
                    }
                    segmentScheduleAdapter.submitList(segmentList)
                }
            }
        }
    }
}