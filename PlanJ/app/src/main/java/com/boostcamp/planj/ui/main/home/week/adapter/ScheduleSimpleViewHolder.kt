package com.boostcamp.planj.ui.main.home.week.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemScheduleBinding

class ScheduleSimpleViewHolder(private val binding: ItemScheduleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Schedule) {
        binding.schedule = item
        binding.executePendingBindings()
        binding.cbDone.isEnabled = false
        binding.cbDone.visibility = View.GONE
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