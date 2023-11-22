package com.boostcamp.planj.ui.main.home.week.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.databinding.ItemWeekScheduleBinding
import com.boostcamp.planj.databinding.ItemWeekStartAndEndBinding

class WeekScheduleAdapter(
    private val scheduleTypeList: MutableList<ScheduleType>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            0 -> {
                return StartAndEndScheduleViewHolder(
                    ItemWeekStartAndEndBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    ), 0
                )
            }

            2 -> {
                return StartAndEndScheduleViewHolder(
                    ItemWeekStartAndEndBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    ), 2
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
            0, 2 -> {
                (holder as StartAndEndScheduleViewHolder).bind(scheduleTypeList[position].schedule)
            }

            1 -> {
                (holder as TodayScheduleViewHolder).bind(scheduleTypeList[position].schedule)
            }
        }
    }

}