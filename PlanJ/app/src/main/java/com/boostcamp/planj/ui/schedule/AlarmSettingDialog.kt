package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.DialogAlarmSettingBinding

class AlarmSettingDialog : DialogFragment() {

    private lateinit var binding: DialogAlarmSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAlarmSettingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVisibility()
        setListener()
    }

    private fun setVisibility() {
        with(binding) {
            rgAlarm.setOnCheckedChangeListener { _, i ->
                layoutAlarmDeparture.visibility =
                    if (i == R.id.rb_alarm_departure) View.VISIBLE else View.GONE
                layoutAlarmEnd.visibility =
                    if (i == R.id.rb_alarm_end) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setListener() {
        with(binding) {
            tvRepeatCancel.setOnClickListener {
                dismiss()
            }

            tvRepeatComplete.setOnClickListener {
                // TODO: 알람 정보 schedule 액티비티로 전달
            }
        }
    }
}