package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.DialogAlarmSettingBinding

class AlarmSettingDialog : DialogFragment() {

    private var _binding: DialogAlarmSettingBinding? = null
    private val binding get() = _binding!!

    private var alarmSettingDialogListener: AlarmSettingDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAlarmSettingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarm = arguments?.getString("alarmInfo")

        setVisibility()
        setListener()
        setBtnClicked(alarm)
    }

    override fun onResume() {
        super.onResume()

        context?.setDialogSize(this)
    }

    private fun setVisibility() {
        with(binding) {
            rgDialogAlarmMode.setOnCheckedChangeListener { _, i ->
                layoutDialogAlarmDeparture.visibility =
                    if (i == R.id.rb_dialog_alarm_departure) View.VISIBLE else View.GONE
                layoutAlarmEnd.visibility =
                    if (i == R.id.rb_dialog_alarm_end) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setListener() {
        with(binding) {
            tvDialogAlarmCancel.setOnClickListener {
                dismiss()
            }

            tvDialogAlarmComplete.setOnClickListener {
                val alarm = if (binding.rbDialogAlarmNo.isChecked) {
                    null
                } else if (binding.rbDialogAlarmEnd.isChecked) {
                    "종료 시간 ${binding.etDialogAlarmBeforeEnd.text}분 전"
                } else {
                    "출발 시간 ${binding.etDialogAlarmBeforeDeparture.text}분 전"
                }
                alarmSettingDialogListener?.onClickComplete(alarm)
                dismiss()
            }
        }
    }

    private fun setBtnClicked(alarm: String?) {
        with(binding) {
            if (alarm == null || alarm == "설정 안함") {
                rbDialogAlarmNo.isChecked = true
            } else if (alarm.startsWith("종료")) {
                rbDialogAlarmEnd.isChecked = true
                etDialogAlarmBeforeEnd.setText(alarm.split(" ")[2].dropLast(1))
            } else {
                rbDialogAlarmDeparture.isChecked = true
                etDialogAlarmBeforeEnd.setText(alarm.split(" ")[2].dropLast(1))
            }
        }
    }

    fun setAlarmSettingDialogListener(listener: AlarmSettingDialogListener) {
        alarmSettingDialogListener = listener
    }
}