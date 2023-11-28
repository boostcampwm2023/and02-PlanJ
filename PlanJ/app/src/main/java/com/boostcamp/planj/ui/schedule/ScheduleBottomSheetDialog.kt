package com.boostcamp.planj.ui.schedule

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.boostcamp.planj.databinding.ScheduleBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScheduleBottomSheetDialog() : BottomSheetDialogFragment() {
    private var _binding : ScheduleBottomSheetBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScheduleBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.npScheduleBottom.minValue = 0
        binding.npScheduleBottom.maxValue = 60
        binding.npScheduleBottom.value = 30

        binding.btnScheduleBottom.setOnClickListener {
            Toast.makeText(requireContext(), "버튼 클릭", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}