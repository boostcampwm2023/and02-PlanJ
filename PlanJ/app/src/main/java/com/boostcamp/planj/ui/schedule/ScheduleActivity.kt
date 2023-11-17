package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.databinding.ActivityScheduleBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        setContentView(binding.root)
        setObserver()
        setListener()
    }

    private fun setObserver() {
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
                        categoryList.getOrNull(0)
                    )
                    (binding.layoutScheduleCategory.editText as MaterialAutoCompleteTextView).setSimpleItems(
                        categoryList.toTypedArray()
                    )
                }
            }
        }
    }

    private fun setListener() {
        binding.tbSchedule.setOnMenuItemClickListener {
            viewModel.saveSchedule()
            true
        }

        binding.tbSchedule.setNavigationOnClickListener {
            finish()
        }
    }
}