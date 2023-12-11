package com.boostcamp.planj.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.SaveDate
import com.boostcamp.planj.databinding.ItemDayOfWeekBinding

class CalendarAdapterViewHolder(private val binding: ItemDayOfWeekBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: SaveDate, onClickListener: OnClickListener) {
        val dayOfWeek = when (absoluteAdapterPosition) {
            0 -> "일"
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            6 -> "토"
            else -> "NULL"
        }
        binding.tvDayOfWeek.text = dayOfWeek
        binding.tvDay.text = item.day.toString()
        binding.item = item

        itemView.setOnClickListener {
            onClickListener.onClick(
                "${item.year}-${
                    String.format(
                        "%02d",
                        item.month
                    )
                }-${String.format("%02d", item.day)}"
            )
        }
    }


    companion object {
        fun from(parent: ViewGroup): CalendarAdapterViewHolder {
            return CalendarAdapterViewHolder(
                ItemDayOfWeekBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}