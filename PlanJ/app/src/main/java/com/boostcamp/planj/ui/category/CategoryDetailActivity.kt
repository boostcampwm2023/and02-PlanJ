package com.boostcamp.planj.ui.category

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.databinding.ActivityCategoryBinding
import com.boostcamp.planj.ui.main.ScheduleClickListener
import com.boostcamp.planj.ui.main.SwipeListener
import com.boostcamp.planj.ui.main.adapter.SegmentScheduleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val args by navArgs<CategoryDetailActivityArgs>()
    private val viewModel: CategoryDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val title = args.categoryId
        viewModel.setTitle(title)
    }

}