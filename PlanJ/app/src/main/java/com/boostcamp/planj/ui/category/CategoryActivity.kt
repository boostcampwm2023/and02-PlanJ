package com.boostcamp.planj.ui.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boostcamp.planj.databinding.ActivityCategoryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}