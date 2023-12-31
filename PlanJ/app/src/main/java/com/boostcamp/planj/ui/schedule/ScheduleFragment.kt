package com.boostcamp.planj.ui.schedule

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentScheduleBinding
import com.boostcamp.planj.ui.alarm.PlanjAlarm
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: ScheduleFragmentArgs by navArgs()

    private val datePickerBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }

    private val timePickerBuilder by lazy {
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
    }

    private val planjAlarm by lazy {
        PlanjAlarm(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        if (args.startLocation != null) {
            viewModel.setStartLocation(args.startLocation!!)
        } else if (args.endLocation != null) {
            viewModel.setEndLocation(args.endLocation!!)
        } else if (args.participants != null) {
            viewModel.setParticipants(args.participants!!.toList())
        }

        viewModel.getCategories()

        setObserver()
        setListener()

        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.scheduleState.collectLatest { scheduleState ->
                updateToolbar(scheduleState)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isComplete.collectLatest { isComplete ->
                if (isComplete) activity?.finish()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.alarmEventFlow.collectLatest { alarmEvent ->
                when (alarmEvent) {
                    is AlarmEvent.Set -> {
                        val alarmInfo = alarmEvent.alarmInfo
                        planjAlarm.setAlarm(alarmInfo)
                    }

                    is AlarmEvent.Delete -> {
                        planjAlarm.deleteAlarm(alarmEvent.scheduleId)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categoryList.collectLatest { categoryList ->
                val arrayAdapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.item_dropdown,
                        categoryList.map { it.categoryName })
                binding.actvScheduleSelectedCategory.setText(viewModel.scheduleCategory.value)
                binding.actvScheduleSelectedCategory.setAdapter(arrayAdapter)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scheduleAlarm.collectLatest { alarm ->
                    if (alarm != null && alarm.alarmType == "DEPARTURE") {
                        binding.tvScheduleLocationAlarm.text = "위치 알람 해제"
                        binding.tvScheduleLocationAlarm.setBackgroundResource(R.drawable.round_r8_red)

                    } else {
                        binding.tvScheduleLocationAlarm.text = "위치 알람 설정"
                        binding.tvScheduleLocationAlarm.setBackgroundResource(R.drawable.round_r8_main2)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.participants.collectLatest {
                    initAdapter()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showToast.collectLatest { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initAdapter() {
        val adapter = ScheduleParticipantProfileAdapter(viewModel.participants.value)
        binding.rvScheduleParticipants.adapter = adapter
    }

    private fun setListener() {
        binding.toolbarSchedule.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_schedule_edit -> {
                    viewModel.startEditingSchedule()
                    true
                }

                R.id.item_schedule_delete -> {
                    lifecycleScope.launch {
                        if (viewModel.deleteSchedule()) {
                            activity?.finish()
                        }
                    }
                    true
                }

                R.id.item_schedule_complete -> {
                    viewModel.completeEditingSchedule()
                    true
                }

                R.id.item_schedule_exit -> {
                    lifecycleScope.launch {
                        if (viewModel.deleteSchedule()) {
                            activity?.finish()
                        }
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.toolbarSchedule.setNavigationOnClickListener {
            activity?.finish()
        }

        binding.tvScheduleRepetitionSetting.setOnClickListener {
            val action =
                ScheduleFragmentDirections.actionScheduleFragmentToRepetitionSettingDialog(viewModel.scheduleRepetition.value)
            findNavController().navigate(action)
        }

        binding.tvScheduleAlarmSetting.setOnClickListener {
            val action =
                ScheduleFragmentDirections.actionScheduleFragmentToAlarmSettingDialog(viewModel.scheduleAlarm.value)
            findNavController().navigate(action)
        }

        binding.ivScheduleMap.setOnClickListener {
            val action =
                ScheduleFragmentDirections.actionScheduleFragmentToScheduleMapFragment(viewModel.endScheduleLocation.value)
            findNavController().navigate(action)
        }

        binding.ivScheduleStartMap.setOnClickListener {
            val action =
                ScheduleFragmentDirections.actionScheduleFragmentToScheduleStartMapFragment(
                    endLocation = viewModel.endScheduleLocation.value,
                    startLocation = viewModel.startScheduleLocation.value
                )
            findNavController().navigate(action)
        }

        binding.tvScheduleAllParticipants.setOnClickListener {
            val action =
                ScheduleFragmentDirections.actionScheduleFragmentToScheduleParticipantsFragment(
                    viewModel.participants.value.toTypedArray(),
                    viewModel.scheduleState.value.isEditMode && viewModel.scheduleState.value.isAuthor
                )
            findNavController().navigate(action)
        }

        binding.tvScheduleLocationUrlScheme.setOnClickListener {
            activity?.let {

                val startLocation =
                    viewModel.startScheduleLocation.value ?: return@setOnClickListener
                val endLocation = viewModel.endScheduleLocation.value ?: return@setOnClickListener
                val url =
                    "nmap://route/public?slat=${startLocation.latitude}&slng=${startLocation.longitude}&sname=${startLocation.placeName}&dlat=${endLocation.latitude}&dlng=${endLocation.longitude}&dname=${endLocation.placeName}&appname=com.boostcamp.planj"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)

                try {
                    it.startActivity(intent)
                } catch (e: Exception) {
                    if (e is ActivityNotFoundException) {
                        it.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.nhn.android.nmap")
                            )
                        )
                    } else {
                        Log.d("PLANJDEBUG", "${e.message}")
                    }
                }

            }
        }

        binding.tvScheduleLocationAlarm.setOnClickListener {
            viewModel.response.value?.let {
                if (binding.tvScheduleLocationAlarm.text == "위치 알람 해제") {
                    viewModel.setAlarm(null)
                } else {
                    val action =
                        ScheduleFragmentDirections.actionScheduleFragmentToScheduleBottomSheetDialog(
                            it.route.trafast[0].summary.duration
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun updateToolbar(scheduleState: ScheduleState) {
        with(binding.toolbarSchedule.menu) {
            findItem(R.id.item_schedule_edit).isVisible =
                (!scheduleState.isEditMode && scheduleState.isEditable)
            findItem(R.id.item_schedule_delete).isVisible =
                (!scheduleState.isEditMode && scheduleState.isAuthor && scheduleState.isDeletable)
            findItem(R.id.item_schedule_exit).isVisible =
                (!scheduleState.isEditMode && !scheduleState.isAuthor && scheduleState.isDeletable)
            findItem(R.id.item_schedule_complete).isVisible = scheduleState.isEditMode
        }
        binding.tvScheduleTop.text = if (!scheduleState.isEditMode) "일정" else "일정 편집"
    }

    fun setStartTime() {
        var dateMillis = 0L
        val datePicker = setDatePicker(viewModel.getStartDate())
        datePicker.show(childFragmentManager, "시작 날짜 설정")

        val scheduleStartTime = viewModel.scheduleStartTime.value
        val timePicker = setTimePicker(scheduleStartTime?.hour ?: 0, scheduleStartTime?.minute ?: 0)

        datePicker.addOnPositiveButtonClickListener {
            dateMillis = datePicker.selection ?: 0
            timePicker.show(childFragmentManager, "시작 시간 설정")
        }
        timePicker.addOnPositiveButtonClickListener {
            viewModel.setStartTime(dateMillis, timePicker.hour, timePicker.minute)
        }
    }

    fun setEndTime() {
        var dateMillis = 0L
        val datePicker = setDatePicker(viewModel.getEndDate() + KST_MILLIS)
        datePicker.show(childFragmentManager, "시작 날짜 설정")

        val scheduleEndTime = viewModel.scheduleEndTime.value
        val timePicker = setTimePicker(scheduleEndTime.hour, scheduleEndTime.minute)

        datePicker.addOnPositiveButtonClickListener {
            dateMillis = datePicker.selection ?: 0
            timePicker.show(childFragmentManager, "시작 시간 설정")
        }
        timePicker.addOnPositiveButtonClickListener {
            viewModel.setEndTime(dateMillis, timePicker.hour, timePicker.minute)
        }
    }

    private fun setDatePicker(selectedDate: Long?): MaterialDatePicker<Long> {
        if (selectedDate != null) {
            datePickerBuilder.setSelection(selectedDate)
        }
        return datePickerBuilder.build()
    }

    private fun setTimePicker(hour: Int = 0, minute: Int = 0): MaterialTimePicker {
        timePickerBuilder.setHour(hour)
        timePickerBuilder.setMinute(minute)
        return timePickerBuilder.build()
    }

    companion object {
        const val KST_MILLIS = 1000 * 60 * 60 * 9
    }
}