package com.boostcamp.planj.ui.schedule

import com.boostcamp.planj.data.model.Participant

interface ScheduleParticipantsListener {

    fun onCheckedChanged(isChecked: Boolean, participant: Participant)
}