package com.boostcamp.planj.ui.schedule

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.databinding.DialogAddDialogBinding
import okhttp3.internal.notifyAll

class ScheduleDialog(
    private val categoryNames : List<String>,
    private val listener: (Schedule) -> Unit
) : DialogFragment() {
    private var _binding: DialogAddDialogBinding? = null
    private val binding :DialogAddDialogBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog)
        isCancelable = true
        Log.d("ScheduleDialog", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ScheduleDialog", "onCreateView")
        _binding = DialogAddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ScheduleDialog", "onViewCreated $categoryNames")
        initAdapter()
        requestFocus()
        setListener()
    }

    override fun onStop() {
        Log.d("ScheduleDialog", "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ScheduleDialog", "onDestroy")
    }


    override fun onDestroyView() {
        _binding = null
        Log.d("ScheduleDialog", "onDestroyView")
        super.onDestroyView()
    }

    private fun initAdapter() {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, categoryNames)
        binding.actvDialogScheduleCategorySelect.setAdapter(arrayAdapter)
    }

    private fun setListener() {
        binding.tvDialogCategoryCancel.setOnClickListener {
            dismiss()
        }

        binding.tvDialogScheduleSuccess.setOnClickListener {
            val category = binding.actvDialogScheduleCategorySelect.text.toString()
            val title = binding.tietDialogScheduleInputTitleSchedule.text.toString()
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

    private fun requestFocus() {
        binding.tietDialogScheduleInputTitleSchedule.requestFocus()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.tietDialogScheduleInputTitleSchedule.windowInsetsController?.show(
                WindowInsetsCompat.Type.ime()
            )
        } else {
            activity?.let {
                WindowInsetsControllerCompat(
                    it.window,
                    binding.tietDialogScheduleInputTitleSchedule
                )
                    .show(WindowInsetsCompat.Type.ime())
            }
        }
    }

}