package com.boostcamp.planj.ui.main

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.ItemDayBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CalendarVO(
    var dayNumber: String = "", 
    var dayOfWeek: String = ""
)

class CalendarAdapter(private val cList: List<CalendarVO>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(private val binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: CalendarVO) {
            binding.tvDayNumber.text = item.dayNumber
            binding.tvDayWeek.text = item.dayOfWeek

            var today = binding.tvDayNumber.text


            val now = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko")))
            if (today == now) {
                binding.tvDayWeek.setBackgroundColor(R.color.main1)
                binding.tvDayNumber.setBackgroundColor(R.color.main1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(cList[position])
    }

    override fun getItemCount(): Int {
        return cList.size
    }

}