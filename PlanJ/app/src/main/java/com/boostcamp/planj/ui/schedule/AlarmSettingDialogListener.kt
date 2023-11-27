package com.boostcamp.planj.ui.schedule

import com.boostcamp.planj.data.model.Alarm

interface AlarmSettingDialogListener {

    fun onClickComplete(alarm: Alarm?)
}