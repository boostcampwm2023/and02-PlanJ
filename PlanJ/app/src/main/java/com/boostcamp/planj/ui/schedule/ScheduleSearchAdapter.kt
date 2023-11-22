package com.boostcamp.planj.ui.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.Address

class ScheduleSearchAdapter(private val onClickListener: SearchMapClickListener) : ListAdapter<Address, ScheduleSearchAdapterViewHolder>(diffUtil){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleSearchAdapterViewHolder {
        return ScheduleSearchAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScheduleSearchAdapterViewHolder, position: Int) {
        holder.onBind(currentList[position], onClickListener)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Address>() {
            override fun areContentsTheSame(
                oldItem: Address,
                newItem: Address
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: Address,
                newItem: Address
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}