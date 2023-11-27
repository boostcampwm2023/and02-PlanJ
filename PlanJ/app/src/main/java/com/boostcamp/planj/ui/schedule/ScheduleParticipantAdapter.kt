package com.boostcamp.planj.ui.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Participant

class ScheduleParticipantAdapter(private val members: List<Participant>) :
    RecyclerView.Adapter<ScheduleParticipantAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleParticipantAdapterViewHolder {
        return ScheduleParticipantAdapterViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: ScheduleParticipantAdapterViewHolder, position: Int) {
        holder.bind(members[position])
    }
}