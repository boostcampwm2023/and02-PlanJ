package com.boostcamp.planj.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.databinding.ItemParticipantProfileBinding

class ScheduleParticipantProfileAdapterViewHolder(private val binding: ItemParticipantProfileBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(participant: Participant) {
        binding.participant = participant
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ScheduleParticipantProfileAdapterViewHolder {
            return ScheduleParticipantProfileAdapterViewHolder(
                ItemParticipantProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}