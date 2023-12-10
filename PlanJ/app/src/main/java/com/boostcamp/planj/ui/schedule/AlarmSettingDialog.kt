package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.databinding.DialogAlarmSettingBinding

class AlarmSettingDialog(
    private val alarm: Alarm?,
    private val alarmSettingDialogListener: AlarmSettingDialogListener
) : DialogFragment() {

    private var _binding: DialogAlarmSettingBinding? = null
    private val binding get() = _binding!!

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

        setVisibility()
        setListener()
        setBtnClicked()
    }

    override fun onResume() {
        super.onResume()

        context?.setDialogSize(this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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
                    Alarm("END", binding.etDialogAlarmBeforeEnd.text.toString().toInt(), 0)
                } else {
                    Alarm("DEPARTURE", binding.etDialogAlarmBeforeDeparture.text.toString().toInt(), 0)
                }
                alarmSettingDialogListener.onClickComplete(alarm)
                dismiss()
            }
        }
    }

    private fun setBtnClicked() {
        with(binding) {
            if (alarm != null && alarm.alarmType == "END") {
                rbDialogAlarmEnd.isChecked = true
                etDialogAlarmBeforeEnd.setText(alarm.alarmTime.toString())
            } else if (alarm != null && alarm.alarmType == "DEPARTURE") {
                rbDialogAlarmDeparture.isChecked = true
                etDialogAlarmBeforeDeparture.setText(alarm.alarmTime.toString())
            } else {
                rbDialogAlarmNo.isChecked = true
            }
        }
    }
}