package com.boostcamp.planj.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.Schedule
import com.boostcamp.planj.databinding.ItemWeekEndBinding
import com.boostcamp.planj.databinding.ItemWeekScheduleBinding
import com.boostcamp.planj.databinding.ItemWeekStartBinding

class WeekScheduleAdapter(private val scheduleList: List<Schedule>) :
    RecyclerView.Adapter<WeekScheduleAdapter.WeekScheduleViewHolder>() {

    class WeekScheduleViewHolder(private val binding: ItemWeekScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Schedule) {
            binding.vwScheduleTitle.text = item.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekScheduleViewHolder {
        //TODO : 일정 여러가지 보여 줄 뷰홀더 3개 추가해야함
        val binding =
            ItemWeekScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekScheduleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: WeekScheduleViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    class StartScheduleViewHolder(binding: ItemWeekStartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val title = binding.tvWeekstartTitle

        fun bind(item: Schedule) {
            title.text = item.title
        }
    }

    class EndScheduleViewHolder(binding: ItemWeekEndBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val title = binding.tvWeekendTitle

        fun bind(item: Schedule) {
            title.text = item.title
        }
    }

    class TodayScheduleViewHolder(binding: ItemWeekScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val title = binding.vwScheduleTitle

        fun bind(item: Schedule) {
            title.text = item.title
        }
    }

}