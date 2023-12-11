package com.boostcamp.planj.ui.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ScheduleParticipantAdapter(private val listener: ScheduleParticipantsListener) :
    ListAdapter<FriendState, ScheduleParticipantAdapterViewHolder>(diffUtil) {

    var isEditMode = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleParticipantAdapterViewHolder {
        return ScheduleParticipantAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScheduleParticipantAdapterViewHolder, position: Int) {
        holder.bind(getItem(position), isEditMode, listener)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FriendState>() {
            override fun areItemsTheSame(oldItem: FriendState, newItem: FriendState): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FriendState, newItem: FriendState): Boolean {
                return oldItem.participant.email == newItem.participant.email
            }

        }
    }
}