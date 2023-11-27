package com.boostcamp.planj.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.databinding.ItemParticipantBinding

class ScheduleParticipantAdapterViewHolder(private val binding: ItemParticipantBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(participant: User) {
        binding.participant = participant
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