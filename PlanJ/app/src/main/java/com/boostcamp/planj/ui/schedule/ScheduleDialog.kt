package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.AddDialogBinding

class ScheduleDialog : DialogFragment() {
    private lateinit var binding: AddDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog)
        isCancelable = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setAddSchedule(listener: (Schedule) -> Unit) {
        binding.btAddSchedule.setOnClickListener {
            val category = "미분류"
            val title = binding.etSchedule.text.toString()
            listener(
                Schedule(
                    (0..Int.MAX_VALUE).random().toString(),
                    title,
                    null,
                    null,
                    "2023-11-20T18:50:00",
                    category,
                    null,
                    listOf(),
                    null,
                    null,
                    false,
                    false
                )
            )
            dismiss()
        }
    }

}