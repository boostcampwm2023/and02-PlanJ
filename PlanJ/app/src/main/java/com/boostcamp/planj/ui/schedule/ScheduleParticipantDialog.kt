package com.boostcamp.planj.ui.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.databinding.DialogParticipantsBinding

class ScheduleParticipantDialog : DialogFragment() {

    private var _binding: DialogParticipantsBinding? = null
    private val binding get() = _binding!!

    private lateinit var participants: List<Participant>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogParticipantsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val participantsInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("participantsInfo", Participant::class.java)
        } else {
            arguments?.getParcelableArrayList("participantsInfo")
        }

        participants = participantsInfo?.toList() ?: emptyList()
        initAdapter()
        setListener()
    }

    override fun onResume() {
        super.onResume()

        context?.setDialogSize(this, 0.6)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initAdapter() {
        val adapter = ScheduleParticipantAdapter(participants)
        binding.rvDialogParticipantsList.adapter = adapter
    }

    private fun setListener() {
        binding.ivDialogParticipantsClose.setOnClickListener {
            dismiss()
        }
    }
}