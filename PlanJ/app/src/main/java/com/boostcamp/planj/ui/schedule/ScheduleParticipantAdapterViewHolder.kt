package com.boostcamp.planj.ui.schedule

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.databinding.ItemParticipantBinding

class ScheduleParticipantAdapterViewHolder(private val binding: ItemParticipantBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        friendState: FriendState,
        isEditMode: Boolean,
        listener: ScheduleParticipantsListener
    ) {
        binding.participant = friendState.participant

        if (isEditMode) {
            binding.layoutParticipant.visibility = View.VISIBLE
            binding.cbParticipantCheck.isChecked = friendState.isParticipated
            if (friendState.participant.currentUser) binding.cbParticipantCheck.isEnabled = false
            binding.cbParticipantCheck.visibility = View.VISIBLE
            binding.imgParticipantDone.visibility = View.GONE
            binding.imgParticipantNotDone.visibility = View.GONE
        } else {
            if (!friendState.isParticipated) binding.layoutParticipant.visibility = View.GONE
            else {
                binding.cbParticipantCheck.visibility = View.GONE
                if (friendState.participant.isFinished) {
                    binding.imgParticipantDone.visibility = View.VISIBLE
                    binding.imgParticipantNotDone.visibility = View.GONE
                } else {
                    binding.imgParticipantDone.visibility = View.GONE
                    binding.imgParticipantNotDone.visibility = View.VISIBLE
                }
            }
        }
        binding.cbParticipantCheck.setOnCheckedChangeListener { _, isChecked ->
            listener.onCheckedChanged(isChecked, friendState.participant)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ScheduleParticipantAdapterViewHolder {
            return ScheduleParticipantAdapterViewHolder(
                ItemParticipantBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}