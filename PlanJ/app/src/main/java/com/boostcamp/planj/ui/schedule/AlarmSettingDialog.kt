package com.boostcamp.planj.ui.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Alarm
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

        val alarm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("alarmInfo", Alarm::class.java)
        } else {
            arguments?.getParcelable("alarmInfo")
        }

        setVisibility()
        setListener()
        setBtnClicked(alarm)
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
                    Alarm("end", binding.etDialogAlarmBeforeEnd.text.toString().toInt())
                } else {
                    Alarm("departure", binding.etDialogAlarmBeforeDeparture.text.toString().toInt())
                }
                alarmSettingDialogListener?.onClickComplete(alarm)
                dismiss()
            }
        }
    }

    private fun setBtnClicked(alarm: Alarm?) {
        with(binding) {
            if (alarm !=null && alarm.alarmType=="end") {
                rbDialogAlarmEnd.isChecked = true
                etDialogAlarmBeforeEnd.setText(alarm.alarmTime.toString())
            } else if (alarm !=null && alarm.alarmType=="departure") {
                rbDialogAlarmDeparture.isChecked = true
                etDialogAlarmBeforeDeparture.setText(alarm.alarmTime.toString())
            } else {
                rbDialogAlarmNo.isChecked = true
            }
        }
    }

    fun setAlarmSettingDialogListener(listener: AlarmSettingDialogListener) {
        alarmSettingDialogListener = listener
    }
}