package com.boostcamp.planj.ui.schedule

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.boostcamp.planj.R
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
        binding.categoryAuto.background = context.getDrawable(R.drawable.dialog_category_backgroud)
    }



}