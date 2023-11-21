package com.boostcamp.planj.ui.main

import android.os.Bundle
import android.text.Layout.Directions
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.findNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentCategoryBinding

import com.boostcamp.planj.ui.category.CategoryDialog
import com.boostcamp.planj.ui.main.adapter.CategoryListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter : CategoryListAdapter
    private val viewModel : CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.view = this
        initAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest {
                    categoryAdapter.submitList(it)
                }
            }
        }
    }

    private fun initAdapter() {
        val clickListener = CategoryClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToCategoryActivity()
            findNavController().navigate(action)
        }
        categoryAdapter = CategoryListAdapter(clickListener)
        binding.rvCategory.adapter = categoryAdapter
        categoryAdapter.submitList(emptyList())
    }

    fun addCategoryDialog(){
        val dialog = CategoryDialog(requireContext())
        dialog.show()
        dialog.setAddCategory {
            viewModel.insertCategory(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}