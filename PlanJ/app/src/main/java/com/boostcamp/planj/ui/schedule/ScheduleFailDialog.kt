package com.boostcamp.planj.ui.schedule

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.DialogAddScheduleBinding
import com.boostcamp.planj.databinding.DialogFailWriteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleFailDialog(
    private val schedule : Schedule,
    private val successListener : (Schedule) -> Unit
) : DialogFragment() {

    private var _binding: DialogFailWriteBinding? = null
    private val binding: DialogFailWriteBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFailWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.schedule = schedule
        setListener()

    }

    override fun onResume() {
        super.onResume()
        dialogFragmentResize(requireContext(), this@ScheduleFailDialog, 0.9f, 0.5f)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setListener(){
        binding.tvDialogScheduleSuccess.setOnClickListener {
            Log.d("PLANJDEBUG", "tvDialogScheduleSuccess clicked")
            if(binding.tietDialogScheduleInputTitleSchedule.text.isNullOrEmpty()){
                binding.tilDialogScheduleInputSchedule.error = "10자 이상 입력해 주세요"
                return@setOnClickListener
            }
            if(binding.tilDialogScheduleInputSchedule.error == null){
                successListener(schedule)
                dismiss()
            }
        }

        binding.tietDialogScheduleInputTitleSchedule.doOnTextChanged { text, _, _, _ ->
            binding.tilDialogScheduleInputSchedule.error =if(!text.isNullOrEmpty() && text.length >= 10){
                  null
            }else {
                "10자 이상 입력해 주세요"
            }
        }
    }


    fun dialogFragmentResize(context: Context, dialogFragment: DialogFragment, width: Float, height: Float) {

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }

}