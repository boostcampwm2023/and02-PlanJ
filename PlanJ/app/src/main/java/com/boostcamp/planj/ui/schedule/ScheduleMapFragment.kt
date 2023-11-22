package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.boostcamp.planj.databinding.FragmentScheduleMapBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleMapFragment : Fragment() {

    private var _binding: FragmentScheduleMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleMapViewModel by viewModels()
    private lateinit var adapter: ScheduleSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleMapBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tilScheduleMapSearch.bringToFront()
        binding.rvScheduleMapSearchList.bringToFront()
        binding.layoutSearchDropDown.bringToFront()
        mapSearch()

        adapter = ScheduleSearchAdapter()
        binding.rvScheduleMapSearchList.adapter = adapter
        binding.rvScheduleMapSearchList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.submitList(emptyList())

        setObserver()
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchMap.collectLatest {
                    if (it.isEmpty()) {
                        binding.rvScheduleMapSearchList.visibility = View.GONE
                    } else {
                        binding.rvScheduleMapSearchList.visibility = View.VISIBLE
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun mapSearch() {
        binding.tietScheduleMapSearchInput.addTextChangedListener { text ->
                text?.let {
                    val query = it.toString().trim()
                    if (!viewModel.clicked.value && query.isNotEmpty()) {
                        viewModel.searchMap(query)
                    }else{
                        viewModel.setEmpty()
                    }
                }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}