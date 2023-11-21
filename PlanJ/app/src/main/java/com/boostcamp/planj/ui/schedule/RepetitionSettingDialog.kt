package com.boostcamp.planj.ui.schedule

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.databinding.DialogRepetitionSettingBinding

class RepetitionSettingDialog : DialogFragment() {

    private var _binding: DialogRepetitionSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRepetitionSettingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repetitionInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("repetitionInfo", Repetition::class.java)
        } else {
            arguments?.getParcelable("repetitionInfo")
        }

        setVisibility()
        setListener()
        setBtnClicked(repetitionInfo)
    }

    override fun onResume() {
        super.onResume()

        context?.setDialogSize(this)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setBtnClicked(repetition: Repetition?) {
        with(binding) {
            if (repetition == null) {
                rbDialogRepetitionNo.isChecked = true
            } else if (repetition.cycleType == "daily") {
                rbDialogRepetitionDay.isChecked = true
                etDialogRepetitionDay.setText(repetition.cycleCount.toString())
            } else {
                rbDialogRepetitionWeek.isChecked = true
                etDialogRepetitionWeek.setText(repetition.cycleCount.toString())
            }
        }
    }

    private fun setVisibility() {
        with(binding) {
            rgDialogRepetitionMode.setOnCheckedChangeListener { _, i ->
                layoutDialogRepetitionDay.visibility =
                    if (i == R.id.rb_dialog_repetition_day) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                layoutDialogRepetitionWeek.visibility =
                    if (i == R.id.rb_dialog_repetition_week) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            tvDialogRepetitionCancel.setOnClickListener {
                dismiss()
            }

            tvDialogRepetitionComplete.setOnClickListener {
                dismiss()
                // TODO: 반복 정보 schedule 액티비티로 전달
            }
        }
    }
}