package com.boostcamp.planj.ui.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.databinding.DialogAddScheduleBinding
import java.util.Calendar

class ScheduleDialog(
    private val categoryNames: List<String>,
    private val initText: String,
    private val isDropdownMode: Boolean,
    private val listener: (String, String, DateTime) -> Unit
) : DialogFragment() {

    private var _binding: DialogAddScheduleBinding? = null
    private val binding: DialogAddScheduleBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.isDropDown = isDropdownMode
        if (!isDropdownMode) {
            binding.actvDialogScheduleCategorySelectNoDropDown.setText(initText)
        }

        initAdapter()
        requestFocus()
        setListener()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initAdapter() {
        if (isDropdownMode) {
            if (categoryNames.isEmpty()) {
                binding.actvDialogScheduleCategorySelect.isEnabled = false
            }
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, categoryNames)
            binding.actvDialogScheduleCategorySelect.setText(initText)
            binding.actvDialogScheduleCategorySelect.setAdapter(arrayAdapter)
        }
    }

    private fun setListener() {
        binding.tvDialogCategoryCancel.setOnClickListener {
            dismiss()
        }

        binding.tvDialogScheduleSuccess.setOnClickListener {
            makeSchedule()
        }

        binding.tietDialogScheduleInputTitleSchedule.addTextChangedListener {
            binding.tietDialogScheduleInputTitleSchedule.error = null
        }

        binding.tietDialogScheduleInputTitleSchedule.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                makeSchedule()
                true
            } else {
                false
            }
        }
    }

    private fun makeSchedule() {
        val category = binding.actvDialogScheduleCategorySelect.text.toString()
        val title = binding.tietDialogScheduleInputTitleSchedule.text.toString()
        if (title.isEmpty()) {
            binding.tietDialogScheduleInputTitleSchedule.error = "비어있습니다."
            return
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        listener(
            category,
            title,
            DateTime(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59
            ),
        )
        dismiss()
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