package com.boostcamp.planj.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.databinding.ItemListScheduleBinding
import com.boostcamp.planj.ui.main.SwipeListener

class SegmentScheduleAdapterViewHolder(private val binding: ItemListScheduleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var item: ScheduleSegment
    lateinit var listener: SwipeListener

    init {
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    listener.swipe(item.scheduleList[position])

                }
            }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvListSchedule)
        }
    }

    fun bind(item: ScheduleSegment, swipeListener: SwipeListener) {
        this.item = item
        this.listener = swipeListener
        binding.tvMainScheduleTitle.text = item.segmentTitle
        val scheduleAdapter = ScheduleAdapter()
        binding.rvListSchedule.adapter = scheduleAdapter
        scheduleAdapter.submitList(this.item.scheduleList)
    }

    companion object {
        fun from(parent: ViewGroup): SegmentScheduleAdapterViewHolder {
            return SegmentScheduleAdapterViewHolder(
                ItemListScheduleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}