package com.boostcamp.planj.ui.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Participant

class ScheduleParticipantProfileAdapter(private val participants: List<Participant>) :
    RecyclerView.Adapter<ScheduleParticipantProfileAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleParticipantProfileAdapterViewHolder {
        return ScheduleParticipantProfileAdapterViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    override fun onBindViewHolder(
        holder: ScheduleParticipantProfileAdapterViewHolder,
        position: Int
    ) {
        holder.bind(participants[position])
    }
}