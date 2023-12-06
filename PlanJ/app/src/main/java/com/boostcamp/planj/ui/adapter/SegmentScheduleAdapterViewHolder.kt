package com.boostcamp.planj.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.ScheduleSegment
import com.boostcamp.planj.databinding.ItemListScheduleBinding

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

    fun bind(
        item: ScheduleSegment,
        swipeListener: SwipeListener,
        clickListener: ScheduleClickListener,
        checkBoxListener: ScheduleDoneListener,
        changeExpanded : (Int) -> Unit
    ) {
        binding.executePendingBindings()
        this.item = item
        this.listener = swipeListener

        binding.tvMainScheduleTitle.text = item.segmentTitle
        val scheduleAdapter = ScheduleAdapter(clickListener, checkBoxListener)
        binding.rvListSchedule.adapter = scheduleAdapter
        binding.ivListToggle.setOnClickListener {
            changeExpanded(bindingAdapterPosition)
        }

        if(item.expanded){
            scheduleAdapter.submitList(this.item.scheduleList)
            binding.ivListToggle.setImageResource(R.drawable.ic_drop_arrow)
        }else{
            scheduleAdapter.submitList(emptyList())
            binding.ivListToggle.setImageResource(R.drawable.ic_arrow_right)
        }

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