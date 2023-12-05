package com.boostcamp.planj.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.FragmentCategoryBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryListAdapter
    private val viewModel: CategoryViewModel by viewModels()

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
        binding.view = this
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.getCategory()
        initAdapter()
        setObserver()

        binding.layoutCategoryDefault.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToCategoryActivity(Category("default", "미분류"))
            findNavController().navigate(action)
        }

        binding.layoutCategoryAll.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToCategoryActivity(Category("all", "전체 일정"))
            findNavController().navigate(action)
        }
    }

    private fun setObserver() {
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
            val action = CategoryFragmentDirections.actionCategoryFragmentToCategoryActivity(it)
            findNavController().navigate(action)
        }
        val popUpMenuListener = object : CategoryPopUpMenuListener {
            override fun delete(category: Category) {
                viewModel.deleteCategory(category)
            }

            override fun edit(title: String) {
                addCategoryDialog(title)
            }
        }
        categoryAdapter = CategoryListAdapter(clickListener, popUpMenuListener)
        binding.rvCategoryCategoryLit.adapter = categoryAdapter
        categoryAdapter.submitList(emptyList())
    }

    fun addCategoryDialog(title: String = "") {
        val dialog = if (title == "") {
            CategoryDialog {
                viewModel.postCategory(it)
            }
        } else {
            CategoryDialog(title) {
                viewModel.patchCategory(it, title)
            }
        }

        activity?.supportFragmentManager?.let { dialog.show(it, null) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}