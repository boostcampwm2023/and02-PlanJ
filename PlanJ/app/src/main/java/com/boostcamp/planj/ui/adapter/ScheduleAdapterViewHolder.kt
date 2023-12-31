package com.boostcamp.planj.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemScheduleBinding
import java.time.LocalDateTime

class ScheduleAdapterViewHolder(private val binding: ItemScheduleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: Schedule,
        clickListener: ScheduleClickListener,
        checkBoxListener: ScheduleDoneListener,
        isCheck: Boolean
    ) {
        binding.isCheck = isCheck
        binding.schedule = item
        binding.executePendingBindings()

        itemView.setOnClickListener {
            clickListener.onClick(item.scheduleId)
        }
        binding.cbDone.setOnCheckedChangeListener { _, _ ->
            checkBoxListener.onClick(item)
        }
    }


    companion object {
        fun from(parent: ViewGroup): ScheduleAdapterViewHolder {
            return ScheduleAdapterViewHolder(
                ItemScheduleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}