package com.boostcamp.planj.ui.schedule

import android.view.LayoutInflater
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
        binding.isEditMode = isEditMode
        binding.isParticipated = friendState.isParticipated

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