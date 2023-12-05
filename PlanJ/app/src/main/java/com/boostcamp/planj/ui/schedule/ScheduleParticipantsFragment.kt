package com.boostcamp.planj.ui.schedule

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
import androidx.navigation.fragment.navArgs
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentScheduleParticipantsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleParticipantsFragment : Fragment() {

    private var _binding: FragmentScheduleParticipantsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleParticipantsViewModel by viewModels()
    private lateinit var adapter: ScheduleParticipantAdapter
    private val args: ScheduleParticipantsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleParticipantsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setParticipants(args.participants?.toList() ?: emptyList())
        initAdapter()
        setObserver()
        setListener()
    }

    private fun initAdapter() {
        adapter = ScheduleParticipantAdapter(viewModel.participantList.value)
        binding.rvScheduleParticipantsList.adapter = adapter
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.participantList.collectLatest {
                    initAdapter()
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEditMode.collectLatest { isEditMode ->
                    updateToolbar(isEditMode)
                }
            }
        }
    }

    private fun updateToolbar(isEditMode: Boolean) {
        with(binding.tbScheduleParticipants.menu) {
            findItem(R.id.item_schedule_edit).isVisible = !isEditMode
            findItem(R.id.item_schedule_delete).isVisible = false
            findItem(R.id.item_schedule_complete).isVisible = isEditMode
        }
    }

    private fun setListener() {
        binding.tbScheduleParticipants.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_schedule_edit -> {
                    viewModel.setEditMode()
                    true
                }

                R.id.item_schedule_complete -> {
                    val action =
                        ScheduleParticipantsFragmentDirections.actionScheduleParticipantsFragmentToScheduleFragment(
                            viewModel.participantList.value.toTypedArray()
                        )
                    findNavController().navigate(action)
                    true
                }

                else -> {
                    false
                }
            }
        }
        binding.tbScheduleParticipants.setNavigationOnClickListener {
            TODO("schedulefragment로 이동")
        }
    }
}