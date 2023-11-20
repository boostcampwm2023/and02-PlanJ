package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.ActivityScheduleBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()

    private val repetitionSettingDialog by lazy {
        RepetitionSettingDialog()
    }

    private val alarmSettingDialog by lazy {
        AlarmSettingDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
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
                    (binding.layoutScheduleCategory.editText as MaterialAutoCompleteTextView).setText(
                        categoryList.getOrNull(categoryList.indexOf(viewModel.selectedCategory))
                    )
                    (binding.layoutScheduleCategory.editText as MaterialAutoCompleteTextView).setSimpleItems(
                        categoryList.toTypedArray()
                    )
                }
            }
        }
    }

    private fun setListener() {
        binding.tbSchedule.setOnMenuItemClickListener { item ->
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

        binding.tbSchedule.setNavigationOnClickListener {
            finish()
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
        with(binding.tbSchedule.menu) {
            findItem(R.id.item_schedule_edit).isVisible = !isEditMode
            findItem(R.id.item_schedule_delete).isVisible = !isEditMode
            findItem(R.id.item_schedule_complete).isVisible = isEditMode
        }
        binding.tvScheduleTop.text = if (!isEditMode) "일정" else "일정 편집"
    }
}