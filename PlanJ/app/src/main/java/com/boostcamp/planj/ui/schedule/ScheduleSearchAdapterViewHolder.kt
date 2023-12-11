package com.boostcamp.planj.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Address
import com.boostcamp.planj.databinding.ItemSearchMapBinding

class ScheduleSearchAdapterViewHolder(private val binding: ItemSearchMapBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: Address, onClickListener: SearchMapClickListener) {
        binding.address = item
        itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ScheduleSearchAdapterViewHolder {
            return ScheduleSearchAdapterViewHolder(
                ItemSearchMapBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}