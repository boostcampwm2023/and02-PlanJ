package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.boostcamp.planj.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    private val args: ScheduleActivityArgs by navArgs()
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        intent.getStringExtra("scheduleId")?.let { scheduleId ->
            viewModel.setScheduleId(scheduleId)
        } ?: viewModel.setScheduleId(args.scheduleId)
    }
}