package com.boostcamp.planj.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemScheduleBinding
import com.boostcamp.planj.ui.main.ScheduleClickListener

class ScheduleSimpleViewHolder(private val binding: ItemScheduleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Schedule, clickListener: ScheduleClickListener) {
        binding.schedule = item
        binding.executePendingBindings()
        itemView.setOnClickListener {
            clickListener.onClick(item)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ScheduleSimpleViewHolder {
            return ScheduleSimpleViewHolder(
                ItemScheduleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}