package com.boostcamp.planj.ui.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.DialogAddDialogBinding

class ScheduleDialog(
    private val listener: (Schedule) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogAddDialogBinding

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
        binding = DialogAddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tietDialogScheduleInputTitleSchedule.requestFocus()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            binding.tietDialogScheduleInputTitleSchedule.windowInsetsController?.show(
                WindowInsetsCompat.Type.ime())
        }else{
            activity?.let {
                WindowInsetsControllerCompat(it.window, binding.tietDialogScheduleInputTitleSchedule)
                    .show(WindowInsetsCompat.Type.ime())
            }
        }
        binding.tvDialogCategoryCancel.setOnClickListener {
            dismiss()
        }

        binding.tvDialogScheduleSuccess.setOnClickListener {
            val category = "미분류"
            val title = binding.actvDialogScheduleCategorySelect.text.toString()
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