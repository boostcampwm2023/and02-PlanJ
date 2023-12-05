package com.boostcamp.planj.ui.categorydetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.boostcamp.planj.databinding.ActivityCategoryBinding
import dagger.hilt.android.AndroidEntryPoint


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

        val category = args.category
        viewModel.setTitle(category)
    }

}