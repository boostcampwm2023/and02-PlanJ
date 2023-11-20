package com.boostcamp.planj.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemWeekEndBinding
import com.boostcamp.planj.databinding.ItemWeekScheduleBinding
import com.boostcamp.planj.databinding.ItemWeekStartBinding

class WeekScheduleAdapter(/*private val scheduleList: List<Schedule>, private val viewNum: Int*/
                          private val scheduleTypeList: MutableList<ScheduleType>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            0 -> {
                return StartScheduleViewHolder(
                    ItemWeekStartBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }


            2 -> {
                return EndScheduleViewHolder(
                    ItemWeekEndBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )

            }

            else -> {
                return TodayScheduleViewHolder(
                    ItemWeekScheduleBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )

            }
        }

    }

    override fun getItemCount(): Int {

        return scheduleTypeList.size
    }

    override fun getItemViewType(position: Int): Int {
        return scheduleTypeList[position].viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (scheduleTypeList[position].viewType) {
            0 -> {
                (holder as StartScheduleViewHolder).bind(scheduleTypeList[position].schedule)
            }

            1 -> {
                (holder as TodayScheduleViewHolder).bind(scheduleTypeList[position].schedule)
            }

            2 -> {
                (holder as EndScheduleViewHolder).bind(scheduleTypeList[position].schedule)
            }
        }
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