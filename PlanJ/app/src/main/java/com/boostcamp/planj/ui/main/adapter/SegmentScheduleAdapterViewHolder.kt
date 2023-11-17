package com.boostcamp.planj.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.ItemTodayScheduleBinding
import com.boostcamp.planj.ui.main.SwipeListener
import com.google.android.material.snackbar.Snackbar

class SegmentScheduleAdapterViewHolder(private val binding : ItemTodayScheduleBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : String, scheduleList: List<Schedule>, swipeListener: SwipeListener){
        binding.tvMainScheduleTitle.text = item

        val scheduleAdapter = ScheduleAdapter()
        binding.rvMainSchedule.adapter = scheduleAdapter

        when(item){
            "일정" -> {
                scheduleAdapter.submitList(scheduleList.filter { !it.finished })
            }
            "완료" -> {
                scheduleAdapter.submitList(scheduleList.filter { it.finished && !it.failed })
            }
            "실패" -> {
                scheduleAdapter.submitList(scheduleList.filter { it.finished && it.failed })
            }
            else -> scheduleAdapter.submitList(scheduleList)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //val position = viewHolder.adapterPosition
//                val book = bookSearchAdapter.currentList[position]
//                viewModel.deleteBook(book)
//                Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
//                    setAction("Undo"){
//                        viewModel.saveBook(book)
//                    }
//                }.show()
                val position = viewHolder.adapterPosition
                swipeListener.swipe(position)

            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvMainSchedule)
        }
    }
    companion object {
        fun from(parent: ViewGroup): SegmentScheduleAdapterViewHolder {
            return SegmentScheduleAdapterViewHolder(
                ItemTodayScheduleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}