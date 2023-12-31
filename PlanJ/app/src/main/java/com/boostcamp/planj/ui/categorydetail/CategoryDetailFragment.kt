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
import com.boostcamp.planj.ui.widget.UpdateWidget
import com.boostcamp.planj.ui.adapter.ScheduleClickListener
import com.boostcamp.planj.ui.adapter.ScheduleDoneListener
import com.boostcamp.planj.ui.adapter.SegmentScheduleAdapter
import com.boostcamp.planj.ui.adapter.SwipeListener
import com.boostcamp.planj.ui.schedule.ScheduleDialog
import com.boostcamp.planj.ui.schedule.ScheduleFailDialog
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
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.getCategoryDetailSchedules()
        initAdapter()
        setObserver()
        setListener()
    }

    private fun setListener() {
        binding.fbCategoryDetailAddSchedule.setOnClickListener {
            val dialog = ScheduleDialog(
                emptyList(),
                initText = if (viewModel.category.value.categoryName == "전체 일정") "미분류" else viewModel.category.value.categoryName,
                false
            ) { category, title, endTime ->
                viewModel.postSchedule(category, title, endTime)
                UpdateWidget.updateWidget(requireContext())
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
            UpdateWidget.updateWidget(requireContext())
        }
        val scheduleClickListener = ScheduleClickListener { scheduleId ->
            val action =
                CategoryDetailFragmentDirections.actionCategoryDetailFragmentToScheduleActivity(
                    scheduleId
                )
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
        segmentScheduleAdapter =
            SegmentScheduleAdapter(swipeListener, scheduleClickListener, checkBoxListener) {
                viewModel.changeExpanded(it)
            }
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
                    viewModel.setScheduleSegment(segmentList)
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
}