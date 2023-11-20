package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.DialogRepetitionSettingBinding

class RepetitionSettingDialog : DialogFragment() {

    private lateinit var binding: DialogRepetitionSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogRepetitionSettingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVisibility()
        setListener()
    }

    private fun setVisibility() {
        with(binding) {
            rgRepeat.setOnCheckedChangeListener { _, i ->
                layoutRepeatDay.visibility =
                    if (i == R.id.rb_repeat_day) View.VISIBLE else View.GONE
                layoutRepeatWeek.visibility =
                    if (i == R.id.rb_repeat_week) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setListener() {
        with(binding) {
            tvRepeatCancel.setOnClickListener {
                dismiss()
            }

            tvRepeatComplete.setOnClickListener {
                // TODO: 반복 정보 schedule 액티비티로 전달
            }
        }
    }
}