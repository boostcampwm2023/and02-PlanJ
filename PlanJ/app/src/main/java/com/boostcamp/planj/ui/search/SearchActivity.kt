package com.boostcamp.planj.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.databinding.ActivitySearchBinding
import com.boostcamp.planj.ui.adapter.ScheduleAdapter
import com.boostcamp.planj.ui.adapter.ScheduleClickListener
import com.boostcamp.planj.ui.adapter.ScheduleDoneListener
import com.boostcamp.planj.ui.schedule.ScheduleActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    val viewModel: SearchViewModel by viewModels()
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        setObserver()
        setListener()
        initAdapter()

        binding.etSearchInputText.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onClickSearch()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredScheduleList.collect { scheduleList ->
                    scheduleAdapter.submitList(scheduleList)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showToast.collect { message ->
                    Toast.makeText(this@SearchActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListener() {
        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }

        binding.etSearchInputText.setOnKeyListener { _, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                viewModel.onClickSearch()
                true
            } else {
                false
            }
        }

        binding.toolbarSearch.setOnMenuItemClickListener {
            viewModel.onClickSearch()
            true
        }
    }

    private fun initAdapter() {
        val intent = Intent(this, ScheduleActivity::class.java)
        val listener = ScheduleClickListener { schedule ->
            intent.putExtra("scheduleId", schedule)
            startActivity(intent)
        }
        scheduleAdapter = ScheduleAdapter(listener, ScheduleDoneListener { schdeule -> }, false)
        binding.rvSearchScheduleList.adapter = scheduleAdapter
    }
}