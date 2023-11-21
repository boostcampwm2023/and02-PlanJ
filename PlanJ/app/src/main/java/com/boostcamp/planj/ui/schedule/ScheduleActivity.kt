package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.ActivityScheduleBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    private var _binding: ActivityScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by viewModels()

    private val repetitionSettingDialog by lazy {
        RepetitionSettingDialog()
    }

    private val alarmSettingDialog by lazy {
        AlarmSettingDialog()
    }

    private val datePickerBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }

    private val timePickerBuilder by lazy {
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setInputMode(INPUT_MODE_KEYBOARD)
            .setInputMode(INPUT_MODE_CLOCK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityScheduleBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)
        setObserver()
        setListener()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEditMode.collect { isEditMode ->
                    updateToolbar(isEditMode)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isComplete.collect { isComplete ->
                    if (isComplete) finish()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                    finish()
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
            finish()
        }

        binding.tvScheduleStartEmpty.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getStartDate())
            datePicker.show(supportFragmentManager, "시작 날짜 설정")
            val timePicker = setTimePicker(viewModel.scheduleStartTime.value)
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setStartDate(datePicker.selection ?: 0)
                timePicker.show(supportFragmentManager, "시작 시간 설정")
            }
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setStartTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleStartDate.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getStartDate())
            datePicker.show(supportFragmentManager, "시작 날짜 설정")
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setStartDate(datePicker.selection ?: 0)
            }
        }

        binding.tvScheduleEndDate.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getEndDate())
            datePicker.show(supportFragmentManager, "종료 날짜 설정")
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setEndDate(datePicker.selection ?: 0)
            }
        }

        binding.tvScheduleStartTime.setOnClickListener {
            val timePicker = setTimePicker(viewModel.scheduleStartTime.value)
            timePicker.show(supportFragmentManager, "시작 시간 설정")
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setStartTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleEndTime.setOnClickListener {
            val timePicker = setTimePicker(viewModel.scheduleEndTime.value)
            timePicker.show(supportFragmentManager, "종료 시간 설정")
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setEndTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleRepetitionSetting.setOnClickListener {
            repetitionSettingDialog.show(supportFragmentManager, "반복 설정")
        }

        binding.tvScheduleAlarmSetting.setOnClickListener {
            alarmSettingDialog.show(supportFragmentManager, "알림 설정")
        }

        binding.ivScheduleMap.setOnClickListener {
            // TODO: 지도 이동
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
}