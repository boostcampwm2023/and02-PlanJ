package com.boostcamp.planj.ui.schedule

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.AddDialogBinding

class ScheduleDialog(private val context : Context) : Dialog(context) {
    private lateinit var binding : AddDialogBinding

    init {
        setCanceledOnTouchOutside(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //binding.etSchedule.requestFocus()

        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setAddSchedule(listener : (Schedule) -> Unit){
        binding.btAddSchedule.setOnClickListener {
            val category = "미분류"
            val title = binding.etSchedule.text.toString()
            listener(Schedule((0..Int.MAX_VALUE).random().toString(), title, null, null, "2023-11-20T18:50:00", category, null, listOf(), null, null, false, false))
            dismiss()
        }
    }

}