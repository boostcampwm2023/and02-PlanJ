package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boostcamp.planj.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
    }
}