package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.DialogAlarmSettingBinding

class AlarmSettingDialog : DialogFragment() {

    private var _binding: DialogAlarmSettingBinding? = null
    private val binding get() = _binding!!
    private val args: AlarmSettingDialogArgs by navArgs()

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
        setBtnClicked(args.alarm)
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
                    "설정 안함"
                } else if (binding.rbDialogAlarmEnd.isChecked) {
                    "종료 시간 ${binding.etDialogAlarmBeforeEnd.text}분 전"
                } else {
                    "출발 시간 ${binding.etDialogAlarmBeforeDeparture.text}분 전"
                }
                Log.d("alarmInfo", alarm)
                dismiss()
                // TODO: 알람 정보 schedule 액티비티로 전달
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
}