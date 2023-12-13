package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.databinding.ScheduleBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScheduleBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: ScheduleBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val args: ScheduleBottomSheetDialogArgs by navArgs()
    private val viewModel: ScheduleViewModel by activityViewModels()

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

        setText(args.time)
        setListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setText(time: Int) {
        var mTime = time
        val hour = mTime / (1000 * 60 * 60)
        mTime %= (1000 * 60 * 60)
        val min = mTime / (1000 * 60)

        binding.tvScheduleBottomExpectTime.text = if (hour == 0) {
            "소요 시간 ${min}분"
        } else {
            "소요 시간 ${hour}시 ${min}분"
        }
    }

    private fun setListener() {
        binding.btnScheduleBottom.setOnClickListener {
            viewModel.setAlarm(Alarm("DEPARTURE", binding.npScheduleBottom.value, 0))
            dismiss()
        }
    }
}