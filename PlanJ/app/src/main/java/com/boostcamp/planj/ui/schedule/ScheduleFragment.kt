package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.databinding.FragmentScheduleBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : Fragment(), RepetitionSettingDialogListener, AlarmSettingDialogListener {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args : ScheduleFragmentArgs by navArgs()

    private val repetitionSettingDialog by lazy {
        RepetitionSettingDialog()
    }

    private val alarmSettingDialog by lazy {
        AlarmSettingDialog()
    }

    private val participantDialog by lazy {
        ScheduleParticipantDialog()
    }

    private val datePickerBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }

    private val timePickerBuilder by lazy {
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
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
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setLocation(args.location)

        initAdapter()
        setObserver()
        setListener()

        repetitionSettingDialog.setRepetitionDialogListener(this)
        alarmSettingDialog.setAlarmSettingDialogListener(this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initAdapter() {
        val adapter = ScheduleParticipantAdapter(viewModel.members.value)
        binding.rvScheduleParticipants.adapter = adapter
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isEditMode.collect { isEditMode ->
                updateToolbar(isEditMode)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isComplete.collect { isComplete ->
                if (isComplete) activity?.finish()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categoryList.collect { categoryList ->
                (binding.tilScheduleCategory.editText as MaterialAutoCompleteTextView).setText(
                    categoryList.getOrNull(categoryList.indexOf(viewModel.selectedCategory))
                )
                (binding.tilScheduleCategory.editText as MaterialAutoCompleteTextView).setSimpleItems(
                    categoryList.toTypedArray()
                )
            }
        }
    }

    private fun setListener() {
        binding.toolbarSchedule.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_schedule_edit -> {
                    viewModel.startEditingSchedule()
                    true
                }

                R.id.item_schedule_delete -> {
                    viewModel.deleteSchedule()
                    activity?.finish()
                    true
                }

                R.id.item_schedule_complete -> {
                    viewModel.completeEditingSchedule()
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

        binding.tvScheduleStartEmpty.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getStartDate())
            datePicker.show(childFragmentManager, "시작 날짜 설정")
            val timePicker = setTimePicker(viewModel.scheduleStartTime.value)
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setStartDate(datePicker.selection ?: 0)
                timePicker.show(childFragmentManager, "시작 시간 설정")
            }
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setStartTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleStartDate.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getStartDate())
            datePicker.show(childFragmentManager, "시작 날짜 설정")
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setStartDate(datePicker.selection ?: 0)
            }
        }

        binding.tvScheduleEndDate.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getEndDate())
            datePicker.show(childFragmentManager, "종료 날짜 설정")
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setEndDate(datePicker.selection ?: 0)
            }
        }

        binding.tvScheduleStartTime.setOnClickListener {
            val timePicker = setTimePicker(viewModel.scheduleStartTime.value)
            timePicker.show(childFragmentManager, "시작 시간 설정")
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setStartTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleEndTime.setOnClickListener {
            val timePicker = setTimePicker(viewModel.scheduleEndTime.value)
            timePicker.show(childFragmentManager, "종료 시간 설정")
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setEndTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleRepetitionSetting.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("repetitionInfo", viewModel.scheduleRepetition.value)
            repetitionSettingDialog.arguments = bundle
            if (!repetitionSettingDialog.isAdded) {
                repetitionSettingDialog.show(childFragmentManager, "반복 설정")
            }
        }

        binding.tvScheduleAlarmSetting.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("alarmInfo", viewModel.scheduleAlarm.value)
            alarmSettingDialog.arguments = bundle
            if (!alarmSettingDialog.isAdded) {
                alarmSettingDialog.show(childFragmentManager, "알림 설정")
            }
        }

        binding.ivScheduleMap.setOnClickListener {
            val action = ScheduleFragmentDirections.actionScheduleFragmentToScheduleMapFragment(viewModel.scheduleLocation.value)
            findNavController().navigate(action)
        }

        binding.tvScheduleAllParticipants.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList("participantsInfo", ArrayList(viewModel.members.value))
            participantDialog.arguments = bundle
            if (!participantDialog.isAdded) {
                participantDialog.show(childFragmentManager, "전체 참가자")
            }
        }
    }

    private fun updateToolbar(isEditMode: Boolean) {
        with(binding.toolbarSchedule.menu) {
            findItem(R.id.item_schedule_edit).isVisible = !isEditMode
            findItem(R.id.item_schedule_delete).isVisible = !isEditMode
            findItem(R.id.item_schedule_complete).isVisible = isEditMode
        }
        binding.tvScheduleTop.text = if (!isEditMode) "일정" else "일정 편집"
    }

    private fun setDatePicker(selectedDate: Long?): MaterialDatePicker<Long> {
        if (selectedDate != null) {
            datePickerBuilder.setSelection(selectedDate)
        }
        return datePickerBuilder.build()
    }

    private fun setTimePicker(selectedTime: String?): MaterialTimePicker {
        if (selectedTime == null) {
            timePickerBuilder.setHour(0)
            timePickerBuilder.setMinute(0)
        } else {
            timePickerBuilder.setHour(selectedTime.split(":")[0].toInt())
            timePickerBuilder.setMinute(selectedTime.split(":")[1].toInt())
        }
        return timePickerBuilder.build()
    }

    override fun onClickComplete(repetition: Repetition?) {
        viewModel.setRepetition(repetition)
    }

    override fun onClickComplete(alarm: Alarm?) {
        viewModel.setAlarm(alarm)
    }
}